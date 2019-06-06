package com.caimao.jserver.server.base;

import java.util.concurrent.atomic.AtomicLong;

public class Sequence {
	private AtomicLong num=new AtomicLong();
	
	private long max=Long.MAX_VALUE;
	
	private int maxBit=Long.bitCount(Long.MAX_VALUE);
	
	public Sequence() {
		
	}
	public Sequence(int maxBit) {
		this.maxBit=maxBit;
		StringBuilder maxBuilder=new StringBuilder();
		for (int i = 0; i < maxBit; i++) {
			maxBuilder.append("9");
		}
		max=Long.parseLong(maxBuilder.toString());
	}
	
	
	
	public long next(){
		long v=num.incrementAndGet();
		//发现大于最大数据，要从头开始
		if(v>max){
			//锁住
			synchronized(num){
				if(num.get()>max){
					num.set(0);
				}
				v=num.incrementAndGet();
			}
		}
		return v;
	}
	
	public String nextStr(){
		return String.format("%0"+maxBit+"d", next());
	}
	
	public static void main(String[] args) {
		Sequence sequence=new Sequence(10);
		System.out.println(sequence.nextStr());
	}
}
