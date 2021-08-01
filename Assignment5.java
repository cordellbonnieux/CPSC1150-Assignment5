public class Assignment5 {
    public static void main(String[] args) {

        // Exercise #2
        System.out.println("Partitician a list by the first element:");
        int[] list = {9, 23, 7, 12, 99, 1, 44, 5, 2, 9, 3, 6, 8};

        System.out.print("Before: ");
        for (int i = 0; i < list.length; i++)
            System.out.print(list[i] + ", ");  
        
        System.out.println();
        
        int something = partition(list);

        System.out.print("After: ");
        for (int i = 0; i < list.length; i++)
            System.out.print(list[i] + ", ");

        System.out.println();

        // Exercise #3 
        String someText = "abcd"; //"Here is a string used for testing.";
        System.out.println("String: " + someText);
        System.out.println("String after: " + reverseDisplay(someText));
        
    }

    /**
     * This method takes an array of integers, assigns the integer at index 0 as
     * the pivot. Then, the method rearranges the array with items less than the pivot being before
     * and items larger than the pivot after. Then it checks the new index of the pivot item
     * and returns it. The method also takes pivot duplicates into consideration, and only returns the
     * index of the first pivot.
     * @param list an array of integers
     * @return an integer representing the new index of the pivot
     */
    public static int partition(int[] list) {

        // create variables
        int pivot = list[0], s = 0, l = 0, p = 0, index = 0;
        int[] smaller = new int[list.length],
            larger = new int[list.length];

        // split the list into two parts, count the number of pivot duplicates
        for (int i = 0; i < list.length; i++)
            if (list[i] < pivot)
                smaller[s++] = list[i];
            else if (list[i] == pivot)
                p++;
            else 
                larger[l++] = list[i];

        // repopulate the list with the new order
        for (int i = 0; i < list.length; i++)
            if (i < s)
                list[i] = smaller[i];
            else if (i <= s + p - 1)
                list[i] = pivot;
            else if (i > s)
                    list[i] = larger[i - s - p];
        
        // record the new index of the first pivot
        for (int i = 0; i < list.length; i++)
            if (list[i] == pivot) {
                index = i;
                break;
            }
        
        return index;
    }

    /**
     * This method takes a string and recursivley reverses it.
     * @param value a String to be reversed
     * @return recusively returns the string minus the character at index 0, until it reaches
     * a length of 1, then it returns itself.
     */
    public static String reverseDisplay(String value) {

        if (value.length() <= 1)
            return value;
        else
            return reverseDisplay(value.substring(1)) + value.charAt(0);
    }
}