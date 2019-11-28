package com.company;
import java.util.concurrent.ForkJoinPool;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.concurrent.ForkJoinTask;

public class Main {
    private static class Pmatrixmul extends ForkJoinTask<int[][]>{
        private int[][] A;
        private int[][] B;
        private int Alines;
        private int Bcolumns;
        private int[][] res = null;
        public Pmatrixmul( int[][] A,  int[][] B,int Bcolumns){
            this.A=A;
            this.B=B;
            this.Alines=A.length;
            this.Bcolumns=Bcolumns;
        }
        @Override
        public int[][] getRawResult() {
            return res;
        }
        @Override
        protected void setRawResult( int[][] value) {
            this.res = value;
        }
        @Override
        protected boolean exec() {
            this.res = new int[Alines][Bcolumns];
             ForkJoinTask<Integer>[][] tasks = new ForkJoinTask[Alines][Bcolumns];
            for (int i = 0; i < Alines; i++)
                for (int j = 0; j < Bcolumns; j++)
                    tasks[i][j] = new Calc(A, B, i, j).fork();
            for (int i = 0; i < Alines; i++)
                for (int j = 0; j < Bcolumns; j++)
                    res[i][j] = tasks[i][j].join();
                return true;

        }

    }
    private static class Calc extends ForkJoinTask<Integer> {
        private  int[][] A;
        private  int[][] B;
        private  int i;
        private  int j;
        private Integer res = null;
        public Calc( int[][] A,  int[][] B,  int i,  int j){
                this.A=A;
                this.B=B;
                this.i=i;
                this.j=j;
        }

        @Override
        public Integer getRawResult() {
            return this.res;
        }

        @Override
        protected void setRawResult( Integer value) {
            this.res = value;
        }
        @Override
        protected boolean exec() {
            int sum = 0;
            for (int k = 0; k < B.length; k++) {
                sum += A[i][k] * B[k][j];
            }
            this.setRawResult(sum);
            return true;
        }
    }

    private static void printM(  int[][] matrix)
    {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(" " + matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    private static int[][] multiplyM( int[][] A,  int[][] B,int Bcolumns)
    {
        int[][] res =new int[ A.length][Bcolumns];
        for (int i = 0; i <  A.length; i++) {
            for (int j = 0; j < Bcolumns; j++) {
                for (int k = 0; k < B.length ; k++){
                    res[i][j]+=A[i][k] * B[k][j];
                }
            }

        }
        return res;

    }
    private static void fillM( int[][] matrix)
    {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                matrix[i][j]=rnd();
            }

        }
    }
    public static int rnd()
    {
        int min =0;
        int max = 75;
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }



    public static void main(String[] args) {
        int[][] A;
        int[][] B;
        int Alines;
        int Acolumns;
        int Bcolumns;
        Scanner scanner = new Scanner(System.in);
        do {
            Alines = scanner.nextInt();
            Acolumns = scanner.nextInt();
            Bcolumns = scanner.nextInt();
        }while((Alines<=0)||(Acolumns<=0)||(Bcolumns<=0));
        A=new  int[Alines][ Acolumns];
        B=new  int[ Acolumns][Bcolumns];
        fillM(A);
        fillM(B);
        long startTime = System.nanoTime() ;
        int[][] res = ForkJoinPool.commonPool().invoke(new Pmatrixmul(A,B, Bcolumns));
        long time = System.nanoTime()  - startTime;
        System.out.println(time);
        startTime = System.nanoTime() ;
        int[][] res1=multiplyM(A,B,Bcolumns);
        time = System.nanoTime()  - startTime;
        System.out.println(time);

    }
}
