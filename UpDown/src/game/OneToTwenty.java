package game;

import java.util.Random;


public class OneToTwenty {
	public static void main(String[] args){
		for(int i : generate(1000)) System.out.print(i + ", ");
	}
	
	private static int[] generate(int size){
		boolean[] numberUsed = new boolean[size];
		int[] array = new int[size];
		int number;
		Random random = new Random();
		for(int i = 0; i < array.length; i++){
			number = random.nextInt(size) + 1;
			while(numberUsed[number - 1]){
				number = random.nextInt(size) + 1;
			}
			array[i] = number;
			numberUsed[number-1] = true;
		}
		return array;
	}
}
