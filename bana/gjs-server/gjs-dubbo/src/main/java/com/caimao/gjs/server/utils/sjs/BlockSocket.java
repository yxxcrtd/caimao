package com.caimao.gjs.server.utils.sjs;

import java.io.*;
import java.net.*;

/**
 * 封装的普通阻塞式Socket
 *
 * @author csl
 */
public class BlockSocket {
    /**
     * 与客户端的输入流
     */
    protected BufferedInputStream bis = null;
    /**
     * 与客户端的输入流
     */
    protected BufferedOutputStream bos = null;
    /**
     * 连接Socket
     */
    protected Socket socket = null;

    /**
     * 连接状态
     */
    public boolean bIsLink = false;

    protected int mHeadLen = 8;

    public BlockSocket() {

    }

    public BlockSocket(Socket sck) throws IOException {
        setSocket(sck);
    }

    public BlockSocket(String sHostIp, int iHostPort) throws IOException {
        Socket sck = new Socket(sHostIp, iHostPort);
        setSocket(sck);
    }

    public BlockSocket(InetSocketAddress addr) throws IOException {
        Socket sck = new Socket(addr.getAddress(), addr.getPort());
        setSocket(sck);
    }

    public void setHeadLen(int iHeadLen) {
        this.mHeadLen = iHeadLen;
    }

    public void closeSocket() {
        try {
            bIsLink = false;

            if (bis != null)
                bis.close();
            if (bis != null)
                bis.close();
            if (this.socket != null)
                this.socket.close();
            this.socket = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接主机
     *
     * @param sHostIp   主机地址
     * @param iHostPort 主机端口
     * @throws Exception
     */
    public void connect(String sHostIp, int iHostPort) throws IOException {
        Socket sck = new Socket(sHostIp, iHostPort);
        setSocket(sck);
    }

    /**
     * 连接主机
     *
     * @param addr InetSocketAddress对象
     * @throws IOException
     */
    public void connect(InetSocketAddress addr) throws IOException {
        Socket sck = new Socket(addr.getAddress(), addr.getPort());
        setSocket(sck);
    }

    /**
     * 设置已经连接成功的Socket
     *
     * @param sck
     * @throws IOException
     */
    protected void setSocket(Socket sck) throws IOException {
        closeSocket();
        this.socket = sck;
        this.bis = new BufferedInputStream(new DataInputStream(this.socket.getInputStream()));
        this.bos = new BufferedOutputStream(new DataOutputStream(this.socket.getOutputStream()));
        bIsLink = true;
    }

    /**
     * 获得可读的字节数
     *
     * @return
     * @throws IOException
     */
    public int available() throws IOException {
        return bis.available();
    }

    protected int writeMsgByLen(byte[] bSendMsg, int iSiginMsgLen) throws IOException, Exception {
        int index = 0;

        //发送长度
        String sLen = StringUtil.FILL("" + bSendMsg.length, '0', this.mHeadLen, 'L');
        bos.write(sLen.getBytes());
        bos.flush();

        //发送报文体
        boolean sendEndFlag = false;
        while (sendEndFlag == false) {
            //当前发送的包
            byte[] temp = new byte[iSiginMsgLen];
            //如果发送的长度大于剩余长度，则直接取剩余长度，则表示发送完毕
            if (iSiginMsgLen > (bSendMsg.length - index)) {
                iSiginMsgLen = bSendMsg.length - index;
                sendEndFlag = true;
            }
            System.arraycopy(bSendMsg, index, temp, 0, iSiginMsgLen);
            bos.write(temp, 0, iSiginMsgLen);
            bos.flush();
            index += iSiginMsgLen;
            Thread.sleep(2);
        }
        return index;
    }

    public byte[] recvMsgByLen(int iLen, int iOverTime) throws IOException, Exception {
        this.socket.setSoTimeout(iOverTime * 3000);

        byte[] recvBuff = new byte[iLen];
        int iRecvedLen = 0;  //已接长度
        int iNotRecvedLen = 0;  //未接长度
        int iCurrRecvLen = 0; //当前接收长度
        int iMaxNum = iOverTime * 1000 / 5;
        int i = 0;
        for (i = 0; i < iMaxNum && iRecvedLen < iLen; i++) {
            //未接长度
            iNotRecvedLen = iLen - iRecvedLen;

            byte[] tmpRecvBuff = new byte[iNotRecvedLen];
            //可接受长度
            iCurrRecvLen = bis.read(tmpRecvBuff);

            if (iCurrRecvLen < 0)
                throw new IOException("接收Socket数据错误.");
            else
                System.arraycopy(tmpRecvBuff, 0, recvBuff, iRecvedLen, iCurrRecvLen);

            iRecvedLen += iCurrRecvLen;

        }

        if (i >= iMaxNum && iRecvedLen < iLen)
            throw new Exception("接收客户端报文超时");

        return recvBuff;
    }


    /**
     * 直接向Socket通道发送字段
     *
     * @param bSendMsg 需要发送的字节数组
     */
    public void directWriteMsg(byte[] bSendMsg) throws IOException, Exception {
        bos.write(bSendMsg, 0, bSendMsg.length);
        bos.flush();
    }

    /**
     * 直接从Socket通道中同步接收指定长度的数据
     *
     * @param iLen      接受长度
     * @param iOverTime 超时时间（秒）
     * @return
     */
    public byte[] directRecvMsg(int iLen, int iOverTime) throws IOException, Exception {
        return recvMsgByLen(iLen, iOverTime);
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isLink() {
        if (socket != null
                && socket.isConnected() == true
                && bIsLink == true)
            return true;
        else
            return false;
    }
}