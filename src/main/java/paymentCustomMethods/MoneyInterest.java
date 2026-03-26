package paymentCustomMethods;

import java.util.Scanner;

public class MoneyInterest {
	double amount, input;
	Scanner sc = new Scanner(System.in);

	private double input() {

		System.out.print("Enter Amount to Calculate S.I.: ");
		input = sc.nextDouble();
		return input;
	}

	public double simpleInterest() {
		int rate = 2, time = 3;
		input();
		double newAmount = (input * rate * time) / 100;
		System.out.println("Simple Interest: " + newAmount);
		return amount;
	}

	double input1, input2;
	int newInput;

	private double input2() {
		System.out.print("\n1. For Addition \n2. For Subtraction \n3. For Multiplication \n4. For Division \nEnter: ");
		newInput = sc.nextInt();
		System.out.print("\nEnter Input value1: ");
		input1 = sc.nextDouble();
		System.out.print("Enter Input value2: ");
		input2 = sc.nextDouble();

		if (newInput == 1) {
			add();
		}
		if (newInput == 2) {
			sub();
		}
		if (newInput == 3) {
			mul();
		}
		if (newInput == 4) {
			div();
		}
		return newInput;
	}

	double sum;

	public void add() {
		sum = input1 + input2;
		System.out.println("Addition of values: " + sum);
	}

	double sub;

	public void sub() {
		sub = input1 - input2;
		System.out.println("Addition of values: " + sub);
	}

	double mul;

	public void mul() {
		mul = input1 * input2;
		System.out.println("Addition of values: " + mul);
	}

	double div;

	public void div() {
		div = input1 / input2;
		System.out.println("Addition of values: " + div);
	}

	public static void main(String[] args) throws Throwable {
		MoneyInterest pay = new MoneyInterest();
		pay.simpleInterest();

		Thread.sleep(1000);
		pay.input2();
	}
}