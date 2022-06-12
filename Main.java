package phonebook;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    static boolean sorted = true;
    static long sortingTime = 0;
    static long maxSortTime = 0;
    static long ms = 0;
    static long s = 0;
    static long m = 0;
    static String stop = "";

    public static void main(String[] args) {
        ArrayList<String> find = new ArrayList<>();
        ArrayList<String> directory = new ArrayList<>();
        File file = new File("C:\\Users\\Funck\\Downloads\\find.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                find.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR");
        }
        file = new File("C:\\Users\\Funck\\Downloads\\directory.txt");
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) {
                directory.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR");
        }



        System.out.println("Start searching (linear search)...");
        long startTime = System.currentTimeMillis();
        int found = linearSearch(find, directory);
        long finishTime = System.currentTimeMillis();
        long searchingTime = finishTime - startTime;
        maxSortTime = searchingTime * 10;
        countTime(searchingTime);
        System.out.printf("Found %d / 500 entries. Time taken: %d min. %d sec. %d ms.\n\n", found, m, s, ms);



        System.out.println("Start searching (bubble sort + jump search)...");
        startTime = System.currentTimeMillis();
        ArrayList<String> sortedDirectory = bubbleSort(directory, startTime);
        sortingTime = System.currentTimeMillis() - startTime;
        if (sorted) {
            found = jumpSearch(find, sortedDirectory);
        } else {
            found = linearSearch(find, directory);
        }
        finishTime = System.currentTimeMillis();
        searchingTime = finishTime - startTime - sortingTime;
        countTime(sortingTime + searchingTime);
        System.out.printf("Found %d / 500 entries. Time taken: %d min. %d sec. %d ms.\n", found, m, s, ms);
        countTime(sortingTime);
        System.out.printf("Sorting time: %d min. %d sec. %d ms.%s\n", m, s, ms, stop);
        countTime(searchingTime);
        System.out.printf("Searching time: %d min. %d sec. %d ms.\n\n", m, s, ms);



        System.out.println("Start searching (quick sort + binary search)...");
        startTime = System.currentTimeMillis();
        sortedDirectory = quickSort(directory);
        sortingTime = System.currentTimeMillis() - startTime;
        found = binarySearch(find, sortedDirectory);
        finishTime = System.currentTimeMillis();
        searchingTime = finishTime - startTime - sortingTime;
        countTime(sortingTime + searchingTime);
        System.out.printf("Found %d / 500 entries. Time taken: %d min. %d sec. %d ms.\n", found, m, s, ms);
        countTime(sortingTime);
        System.out.printf("Sorting time: %d min. %d sec. %d ms.\n", m, s, ms);
        countTime(searchingTime);
        System.out.printf("Searching time: %d min. %d sec. %d ms.\n\n", m, s, ms);



        System.out.println("Start searching (hash table)...");
        startTime = System.currentTimeMillis();
        HashMap<String, String> hashTable = createHashTable(directory);
        sortingTime = System.currentTimeMillis() - startTime;
        found = hashTableSearching(find, hashTable);
        finishTime = System.currentTimeMillis();
        searchingTime = finishTime - startTime - sortingTime;
        countTime(sortingTime + searchingTime);
        System.out.printf("Found %d / 500 entries. Time taken: %d min. %d sec. %d ms.\n", found, m, s, ms);
        countTime(sortingTime);
        System.out.printf("Creating time: %d min. %d sec. %d ms.\n", m, s, ms);
        countTime(searchingTime);
        System.out.printf("Searching time: %d min. %d sec. %d ms.\n\n", m, s, ms);
    }



    public static int linearSearch(ArrayList<String> find, ArrayList<String> directory) {
        int found = 0;
        for (String f : find) {
            for (String d : directory) {
                if (d.endsWith(f)) {
                    found++;
                    break;
                }
            }
        }
        return found;
    }

    public static int jumpSearch(ArrayList<String> find, ArrayList<String> directory) {
        int found = 0;
        for (String person : find) {
            found += jumpSearch(person, directory);
        }
        return found;
    }

    public static int jumpSearch(String person, ArrayList<String> directory) {
        int step = (int) Math.sqrt(directory.size());
        int current = 0;
        int index;
        while (current < directory.size()) {
            if (directory.get(current).endsWith(person)) {
                return 1;
            } else if (directory.get(current).compareTo(person) < 0) {
                index = current - 1;
                while ((index > current - step) && (index >= 1)) {
                    if (directory.get(index).endsWith(person)) {
                        return 1;
                    }
                    index--;
                }
                return 0;
            }
            current = current + step;
        }
        index = directory.size() - 1;
        while (index > current - step) {
            if (directory.get(index).endsWith(person)) {
                return 1;
            }
            index--;
        }
        return 0;
    }

    public static ArrayList<String> bubbleSort(ArrayList<String> list, long startTime) {
        for (int i = 1; i < list.size(); i++) {
            for (int j = 0; j < list.size() - i; j++) {
                if (list.get(j).compareTo(list.get(j + 1)) > 0) {
                    String temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }
            }
            if (System.currentTimeMillis() - startTime > maxSortTime) {
                stop = " - STOPPED, moved to linear search";
                sorted = false;
                break;
            }
        }
        return list;
    }

    public static ArrayList<String> quickSort(ArrayList<String> list) {
        if (list.size() < 2) {
            return list;
        }
        String pivot = list.get(list.size() - 1).replaceAll("\\d+", "").trim();
        ArrayList<String> left = new ArrayList<>();
        ArrayList<String> right = new ArrayList<>();
        for (int i = 0; i < list.size() - 1; i++) {
            String line = list.get(i).replaceAll("\\d+", "").trim();
            if (line.compareTo(pivot) > 0) {
                right.add(list.get(i));
            } else {
                left.add(list.get(i));
            }
        }
        return unite(quickSort(left), pivot, quickSort(right));
    }

    public static int binarySearch(ArrayList<String> find, ArrayList<String> directory) {
        int found = 0;
        for (String person : find) {
            found += binarySearch(person, directory);
        }
        return found;
    }

    public static int binarySearch(String person, ArrayList<String> directory) {
        int left = 0;
        int right = directory.size() - 1;
        while (left <= right) {
            int middle = (left + right) / 2;
            String line = directory.get(middle).replaceAll("\\d+", "").trim();
            if (line.equals(person)) {
                return 1;
            } else if (line.compareTo(person) > 0) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }
        return 0;
    }

    public static HashMap<String, String> createHashTable(ArrayList<String> directory) {
        HashMap<String, String> hashTable = new HashMap<>();
        for (String line : directory) {
            String key = line.replaceAll("\\d+", "").trim();
            String value = line.replaceAll("\\D+", "");
            hashTable.put(key, value);
        }
        return hashTable;
    }

    public static int hashTableSearching(ArrayList<String> find, HashMap<String, String> hashTable) {
        int found = 0;
        for (String person : find) {
            if (hashTable.containsKey(person)) {
                found++;
            }
        }
        return found;
    }

    public static ArrayList<String> unite(ArrayList<String> left, String pivot, ArrayList<String> right) {
        left.add(pivot);
        left.addAll(right);
        return left;
    }

    public static void countTime(long time) {
        ms = time % 1000;
        s = time / 1000 % 60;
        m = time / 60000;
    }
}
