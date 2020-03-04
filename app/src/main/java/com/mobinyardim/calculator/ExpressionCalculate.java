package com.mobinyardim.calculator;

/**
 * Created by Mobin Yardim on 09/03/2018.
 */
import java.util.*;


public class ExpressionCalculate {

    private String operators="+ - * / ( )";

    private boolean isOperator(char ch)
    {
        return operators.indexOf(ch)>-1;
    }

    private boolean isHighOp(char ch1 , char ch2)
    {
        if((ch1=='*'||ch1=='/') && (ch2=='+'||ch2=='-'))
            return true;

        return false;
    }

    private String reverse(String s)
    {
        String arr[]=s.trim().split(" ");
        String newstr = new String();
        int len = arr.length-1;
        System.out.println(len);
        for(int i=0 ; i <len/2+1;i++)
        {
            String temp = arr[i];
            arr[i]=arr[len-i];
            arr[len-i]=temp;
        }
        for(int i=0 ; i<=len ; i++)
        {
            newstr+=(arr[i]+" ");
        }
        return newstr;
    }

    public String spacing(String str)
    {
        StringBuilder s = new StringBuilder(str);
        for(int i=0 ;i< s.length() ; i++)
        {
            if( (i==0 && s.charAt(0)=='-' )||( s.charAt(i)=='-' && s.charAt(i-2)=='(' && i>1 )) {}
            else if(isOperator(s.charAt(i)))
            {
                s.insert(i+1," ");
                try {
                    if(s.charAt(i-1)!=' '){
                        s.insert(i," ");
                        i++;
                    }}catch(Exception e) {}
                i++;
            }

        }
        return s.toString();
    }

    private String infix2Prefix(String str)
    {
        str=str.replaceAll("%", " / 100");
        String arr[]=str.split(" ") , postfix=new String();
        Stack <String>op=new Stack<String>();
        for(int i=arr.length-1 ; i >=0 ; i--)
        {
            if(isOperator(arr[i].charAt(0)) && arr[i].length()<2)
            {
                String last=" ";
                if(!op.isEmpty())last=op.peek();
                if(op.isEmpty() || arr[i].charAt(0)==')')
                {
                    op.push(arr[i]);
                }
                else if(arr[i].charAt(0)=='(')
                {
                    while(op.peek().indexOf(")")==-1)
                    {
                        postfix+=(op.pop()+" ");
                    }
                    op.pop();
                }
                else if( isHighOp(last.charAt(0), arr[i].charAt(0)))
                {
                    while(!op.isEmpty() && isHighOp(op.peek().charAt(0),arr[i].charAt(0)) && arr[i].indexOf(")")==-1)
                    {
                        postfix+=(op.pop()+" ");
                    }
                    op.push(arr[i]);
                }
                else
                {
                    op.push(arr[i]);
                }
            }
            else
            {
                postfix+=arr[i]+" ";
            }
        }
        while(!op.empty())
        {
            postfix+=op.pop()+" ";
        }
        postfix=reverse(postfix);
        return postfix;
    }

    private double calcPrefix(String str)
    {
        str=reverse(str);
        String s[] = str.trim().split(" ");
        Stack <Double> num = new Stack<Double>();
        Stack <Character> op = new Stack<Character>();
        for(int i=0;i<s.length ; i++)
        {
            if(isOperator( s[i].charAt(0)) && s[i].length()<2)	{
                op.push(s[i].charAt(0));
            }
            else {
                num.push(Double.valueOf(s[i]));
            }
            if(num.size()>1 && op.size()>0)
            {
                double a = num.pop() , b =num.pop() , c=0;
                switch(op.pop())
                {
                    case '*':
                        c=a*b;
                        break;
                    case '/':
                        c=a/b;
                        break;
                    case '+':
                        c=a+b;
                        break;
                    case '-':
                        c=a-b;
                        break;
                }
                num.push(c);
            }
        }
        return num.pop();
    }

    public double calcInfix(String str)
    {
        return calcPrefix(infix2Prefix(spacing(str)));
    }
}
