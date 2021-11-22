package com.example.elevator.sock;

package kr.co.company.elevator;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

public class SockClient {

	final int port = 5000;
	final String serverIp = "192.168.5.5";

	Socket socket;
	JSONObject giveObj = new JSONObject();
	JSONObject obj = new JSONObject();
	JSONArray objArray = new JSONArray();

	void send(JSONObject obj){
		try {
			socket = new Socket(serverIp, port);

			OutputStream os = socket.getOutputStream();

			byte cmd = (byte)obj.get("cmd");
			byte length = (byte)obj.get("length");

			byte[] reqBuffer = new byte[7];

			reqBuffer[0] = (byte)0xA5; //STX_1
			reqBuffer[1] = (byte)0x5A; //STX_2
			reqBuffer[2] = length; //length_Low
			reqBuffer[3] = (byte)0x00; //length_High
			reqBuffer[4] = cmd; //Command
			reqBuffer[5] = (byte)0x46; //CRC_Low
			reqBuffer[6] = (byte)0xB1; //CRC_High

			os.write(reqBuffer);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	JSONObject recv() {
		int size = 1024;
		int recvSize = 0;
		int start_id_index = 5;
		int id_size = 7;
		int errorcode_index = 13;
		int start_datetime_index = 14;
		short code = 0;
		short errorcode = 0;

		String cmd = "";
		String ID = "";

		String datetime = "";
		String date = "";
		String time = "";

		try {
			InputStream is = socket.getInputStream();

			byte[] recvBuffer = new byte[size];
			recvSize = is.read(recvBuffer);

			cmd = String.valueOf(recvBuffer[4]);

			for(int i = start_id_index; i <id_size; i++) {
				ID = ID + String.valueOf(recvBuffer[i]);
			}

			while(true) {

				datetime = "";
				date = "";
				time = "";
				code = 0;

				errorcode = 0;
				errorcode |= ((short)recvBuffer[errorcode_index] << 8) & 0xFF00;
				errorcode |= ((short)recvBuffer[errorcode_index - 1]) & 0x00FF;

				recvSize = recvSize -((125 - errorcode) * 8) - 10;

				for(int i = start_datetime_index; i < recvSize; i = i + 8 ) {
					for(int j = 0; j < 3; j++) {
						if (recvBuffer[i + j] < 10) { date = date + "0" + recvBuffer[i + j]; }
						else {date = date + recvBuffer[i + j];}

						if(j != 2) { date = date + "-";}
					}

					for(int j = 3; j < 6; j++) {
						if (recvBuffer[i + j] < 10) { time = time + "0" + recvBuffer[i + j]; }
						else {time = time + recvBuffer[i + j];}

						if(j != 5) { time = time + ":";}
					}

					datetime = date + " " + time;

					code |= ((short)recvBuffer[i + 7] << 8) & 0xFF00;
					code |= ((short)recvBuffer[i + 6]) & 0x00FF;

					JSONObject obj = new JSONObject();
					obj.put("datetime",datetime);
					obj.put("code", code);
					objArray.put(obj);
				}

				if(errorcode == 125) {
					is.read(recvBuffer);
				}
				else {
					break;
				}
			}

			giveObj.put("cmd", cmd);
			giveObj.put("ID", ID);
			giveObj.put("info", objArray);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return giveObj;
	}
}
