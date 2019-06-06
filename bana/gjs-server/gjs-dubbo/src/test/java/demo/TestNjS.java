package demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class TestNjS {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket socket = new Socket("172.32.1.218",8083);
        OutputStream osm = socket.getOutputStream();
        InputStream ism = socket.getInputStream();
        // 注册
        osm.write("015000000000N12345678911".getBytes());
        osm.flush();
        byte b[] = new byte[24];
        ism.read(b);
        System.out.println(new String(b));
        //发送业务
		String cStr = "{\"BANKCODE\":\"005\",\"BANKNO\":\"6227000015910152025\",\"BANKPWD\":\"615932\",\"FIRMCARDNO\":\"142322198906193018\",\"FIRMCARDTYPE\":\"1\",\"FIRMID\":\"163\",\"FULLNAME\":\"宁豪\",\"SIGNBANKNAME\":\"建设银行\",\"TELNO\":\"18210046821\",\"TRADEPWD\":\"ninghao123\",\"TRADERID\":\"16390752\"}";
		//osm.write(("016005000272NN0000000512014009000248NN0000000512" + cStr).getBytes("GBK"));

		osm.write("016005000079NN0000000105011044000055NN0000000105{\"TOKEN\":\"163237131446102848141\",\"TRADERID\":\"16323713\"}".getBytes("GBK"));
		//016005000272NN0000000512014009000248NN0000000512{"BANKCODE":"005","BANKNO":"6227000015910152025","BANKPWD":"615932","FIRMCARDNO":"142322198906193018","FIRMCARDTYPE":"1","FIRMID":"163","FULLNAME":"����","SIGNBANKNAME":"��������","TELNO":"18210046821","TRADEPWD":"ninghao123","TRADERID":"16390752"}

		System.out.println(cStr.length());
		//osm.write("016005000068N12345678911011001000044N15121133942{\"TRADEPASS\":\"123456\",\"TRADERID\":\"16304897\"}".getBytes());
		//osm.write("016005000265NN0000000006014009000241NN0000000006{\"BANKCODE\":\"408\",\"BANKNO\":\"621661280000447287\",\"BANKPWD\":\"123456\",\"FIRMCARDNO\":\"410181198707244057\",\"FIRMCARDTYPE\":\"1\",\"FIRMID\":\"163\",\"FULLNAME\":\"谢利江\",\"SIGNBANKNAME\":\"中国银行\",\"TELNO\":\"18611298962\",\"TRADEPWD\":\"123456\",\"TRADERID\":\"16321756\"}".getBytes());
		//osm.write("016005000079N12345678911014008000055N15121133942{\"TOKEN\":\"163228881445478302562\",\"TRADERID\":\"16322888\"}".getBytes());
        osm.flush();
        ByteArrayOutputStream baosm = new ByteArrayOutputStream();
        read(ism, "", baosm);
        System.out.println(new String(baosm.toByteArray(), "GBK"));
	}
	
	public static void read(InputStream ism, String next, ByteArrayOutputStream baosm) throws IOException {
		byte headerByte[] = new byte[24];
		ism.read(headerByte);
		String headerStr = new String(headerByte);
		int totalLen = Integer.parseInt(headerStr.substring(6, 12));
		byte body[] = new byte[totalLen];
		int curLen = ism.read(body);
		baosm.write(body, 0, curLen);
		while (totalLen > curLen) {
			curLen = ism.read(body);
			baosm.write(body, 0, curLen);
			curLen += curLen;
		}
		next = String.valueOf(headerStr.charAt(12));
		if ("Y".equals(next)) {
			read(ism, next, baosm);
		}
	}

}
