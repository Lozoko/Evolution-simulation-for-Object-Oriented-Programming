package main;

import java.util.*;

public class Randoms {
    private static final int[] defaultGenotype1 = {0,0,0,0,1,1,1,1,2,2,2,2,3,3,3,3,
    4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7} ;
    public static MoveDirection randomDirection(int[] genotype){
        Random rand = new Random();
        int index = rand.nextInt(32);
        int direction = genotype[index];
        return switch(direction){
            case 0 -> MoveDirection.FORWARD;
            case 1 -> MoveDirection.FORWARDRIGHT;
            case 2 -> MoveDirection.RIGHT;
            case 3 -> MoveDirection.BACKWARDRIGHT;
            case 4 -> MoveDirection.BACKWARD;
            case 5 -> MoveDirection.BACKWARDLEFT;
            case 6 -> MoveDirection.LEFT;
            case 7 -> MoveDirection.FORWARDLEFT;
            default -> null;
        };
    }
    public static MapDirection randomMapDirection(){
        OptionsParser chooseDirection = new OptionsParser();
        Random random = new Random();
        int r = random.nextInt(8);
        return chooseDirection.parse(r);
    }
    public static int[] createGenotype(int[] genotype1, int[] genotype2){
        Random rand = new Random();
        int index1 = rand.nextInt(29) + 1;
        int index2 = rand.nextInt(30 - index1) + index1 + 2;

        boolean firstPart = rand.nextBoolean();
        boolean secondPart = rand.nextBoolean();
        boolean thirdPart;
        if(firstPart && secondPart){
            thirdPart = false;
        }
        else if(!firstPart && !secondPart){
            thirdPart = true;
        }
        else{
            thirdPart = rand.nextBoolean();
        }
        int[] result = new int[32];
        for(int i = 0; i< index1;i++){
            if(firstPart){
                result[i] = genotype1[i];
            }
            else{
                result[i] = genotype2[i];
            }
        }
        for(int i = index1; i< index2;i++){
            if(secondPart){
                result[i] = genotype1[i];
            }
            else{
                result[i] = genotype2[i];
            }
        }
        for(int i = index2; i< 32;i++){
            if(thirdPart){
                result[i] = genotype1[i];
            }
            else{
                result[i] = genotype2[i];
            }
        }
        int[] directions = new int[8];
        boolean complete = false;
        while(!complete){
            for(int i=0;i<8;i++){
                directions[i] = 0;
            }
            for (int i : result){
                directions[i]++;
            }
            for(int i=0;i<8;i++){
                if(directions[i] == 0){
                    int index = rand.nextInt(32);
                    result[index] = i;
                    break;
                }
                if(i == 7) complete = true;
            }
        }
        Arrays.sort(result);
        return result;
    }
    public static int[] createStandardGenotype(){
        List<Integer> tmpGenotypeList = new ArrayList<>();
        for(Integer i : defaultGenotype1)
            tmpGenotypeList.add(i);
        Collections.shuffle(tmpGenotypeList);
        int[] tmpGenotypeArray = new int[32];
        for(int i = 0; i < 32; i++)
            tmpGenotypeArray[i] = tmpGenotypeList.get(i);
        return Randoms.createGenotype(Randoms.defaultGenotype1, tmpGenotypeArray);
    }


}
