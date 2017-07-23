package lamports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Shubham Puranik
 */

public class LamportsLogicalClock {
    public static Integer getKey(Set<Map.Entry<Integer,Integer>> entrySet,Integer value){
        Integer key=null;
        for(Map.Entry entry:entrySet){
            if(value.equals(entry.getValue())){
                key=(Integer)entry.getKey();
                break;
            }
        }
        return key;
    }
    public static int[][] getLamportsTimestamp(){
        Scanner in=new Scanner(System.in);
        System.out.println("Enter no. of processes:");
        int noOfProcesses=in.nextInt(),noOfEvents[]=new int[noOfProcesses],clock[][]=new int[noOfProcesses][];
        System.out.println("Enter no. of events per process:");
        for(int i=0;i<noOfProcesses;i++){
            noOfEvents[i]=in.nextInt();
            clock[i]=new int[noOfEvents[i]];
            for(int j=0;j<noOfEvents[i];j++)
                clock[i][j]=j+1;
        }
        System.out.println("Enter no. of relationships:");
        int noOfRelationships=in.nextInt();
        Map<Integer,Integer> map=new HashMap<>();
        System.out.println("Enter happened-before relationships:");
        for(int i=0;i<noOfRelationships;i++){
            String input=in.next();
            map.put(Integer.parseInt(input.split(">")[0]),Integer.parseInt(input.split(">")[1]));
        }
        ArrayList<Integer> keys=new ArrayList<>(map.keySet()),values=new ArrayList<>(map.values());
        Collections.sort(values,new Comparator<Integer>(){
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1%10,o2%10);
            }
        });
        Collections.sort(keys,new Comparator<Integer>(){
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1%10,o2%10);
            }
        });
        for(Integer value:values){
            Integer key=getKey(map.entrySet(),value);
            if(key%10-1>0)
                clock[key/10][key%10]=Integer.max(clock[key/10][key%10],clock[key/10][key%10-1]+1);
            else
                clock[key/10][key%10]=Integer.max(clock[key/10][key%10],key%10+1);
            if(value%10-1>0){
                clock[value/10][value%10]=Integer.max(clock[value/10][value%10-1]+1,clock[key/10][key%10]+1);
                if(value%10+1<noOfEvents[value/10])
                    clock[value/10][value%10+1]=Integer.max(clock[value/10][value%10+1],clock[value/10][value%10]+1);
            }else{
                clock[value/10][value%10]=Integer.max(value%10+1,clock[key/10][key%10]+1);
                if(value%10+1<noOfEvents[value/10])
                    clock[value/10][value%10+1]=Integer.max(clock[value/10][value%10+1],clock[value/10][value%10]+1);
            }
        }
        for(Integer key:keys){
            Integer value=map.get(key);
            if(key%10-1>0)
                clock[key/10][key%10]=Integer.max(clock[key/10][key%10],clock[key/10][key%10-1]+1);
            else
                clock[key/10][key%10]=Integer.max(clock[key/10][key%10],key%10+1);
            if(value%10-1>0){
                clock[value/10][value%10]=Integer.max(clock[value/10][value%10-1]+1,clock[key/10][key%10]+1);
                if(value%10+1<noOfEvents[value/10])
                    clock[value/10][value%10+1]=Integer.max(clock[value/10][value%10+1],clock[value/10][value%10]+1);
            }else{
                clock[value/10][value%10]=Integer.max(value%10+1,clock[key/10][key%10]+1);
                if(value%10+1<noOfEvents[value/10])
                    clock[value/10][value%10+1]=Integer.max(clock[value/10][value%10+1],clock[value/10][value%10]+1);
            }
        }
        return clock;
    }
    public static void main(String[] args) {
        /*
            Test case 1:-
            No. of processes: 4
            No. of events per process: 7 6 6 5
            No. of relationships: 12
            Relationships: 00>30 10>01 20>11 12>21 31>13 02>23 03>32 14>04 33>22 34>15 24>05 06>25
        
            Test case 2:-
            No. of processes: 2
            No. of events per process: 7 5
            No. of relationships: 4
            Relationships: 01>12 11>04 05>14 13>06
        */
        int[][] clock=getLamportsTimestamp();
        for(int i=0;i<clock.length;i++){
            for(int j=0;j<clock[i].length;j++)
                System.out.print(clock[i][j]+" ");
            System.out.println();
        }
    }
}
