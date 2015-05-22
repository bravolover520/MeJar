package me.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.SparseArray;

/**
 * List使用工具类
 * @author Jesus{931178805@qq.com}
 * 2014年7月9日
 */
public class ListUtils {

    /** default join separator **/
    public static final String DEFAULT_JOIN_SEPARATOR = ",";

    /**
     * get size of list
     * 
     * <pre>
     * getSize(null)   =   0;
     * getSize({})     =   0;
     * getSize({1})    =   1;
     * </pre>
     * 
     * @param <V>
     * @param sourceList
     * @return if list is null or empty, return 0, else return {@link List#size()}.
     */
    public static <V> int getSize(List<V> sourceList) {
        return sourceList == null ? 0 : sourceList.size();
    }

    /**
     * is null or its size is 0
     * 
     * <pre>
     * isEmpty(null)   =   true;
     * isEmpty({})     =   true;
     * isEmpty({1})    =   false;
     * </pre>
     * 
     * @param <V>
     * @param sourceList
     * @return if list is null or its size is 0, return true, else return false.
     */
    public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }

    /**
     * compare two list
     * 
     * <pre>
     * isEquals(null, null) = true;
     * isEquals(new ArrayList&lt;String&gt;(), null) = false;
     * isEquals(null, new ArrayList&lt;String&gt;()) = false;
     * isEquals(new ArrayList&lt;String&gt;(), new ArrayList&lt;String&gt;()) = true;
     * </pre>
     * 
     * @param <V>
     * @param actual
     * @param expected
     * @return
     */
    public static <V> boolean isEquals(ArrayList<V> actual, ArrayList<V> expected) {
        if (actual == null) {
            return expected == null;
        }
        if (expected == null) {
            return false;
        }
        if (actual.size() != expected.size()) {
            return false;
        }

        for (int i = 0; i < actual.size(); i++) {
        	if (actual.get(i) == expected.get(i) || (actual.get(i) == null ? expected.get(i) == null : actual.get(i).equals(expected.get(i)))){
        		return false;
            }
        }
        return true;
    }

    /**
     * join list to string, separator is ","
     * 
     * <pre>
     * join(null)      =   "";
     * join({})        =   "";
     * join({a,b})     =   "a,b";
     * </pre>
     * 
     * @param list
     * @return join list to string, separator is ",". if list is empty, return ""
     */
    public static String join(List<String> list) {
        return join(list, DEFAULT_JOIN_SEPARATOR);
    }

    /**
     * join list to string
     * 
     * <pre>
     * join(null, '#')     =   "";
     * join({}, '#')       =   "";
     * join({a,b,c}, ' ')  =   "abc";
     * join({a,b,c}, '#')  =   "a#b#c";
     * </pre>
     * 
     * @param list
     * @param separator
     * @return join list to string. if list is empty, return ""
     */
    public static String join(List<String> list, char separator) {
        return join(list, new String(new char[] {separator}));
    }

    /**
     * join list to string. if separator is null, use {@link #DEFAULT_JOIN_SEPARATOR}
     * 
     * <pre>
     * join(null, "#")     =   "";
     * join({}, "#$")      =   "";
     * join({a,b,c}, null) =   "a,b,c";
     * join({a,b,c}, "")   =   "abc";
     * join({a,b,c}, "#")  =   "a#b#c";
     * join({a,b,c}, "#$") =   "a#$b#$c";
     * </pre>
     * 
     * @param list
     * @param separator
     * @return join list to string with separator. if list is empty, return ""
     */
    public static String join(List<String> list, String separator) {
        if (isEmpty(list)) {
            return "";
        }
        if (separator == null) {
            separator = DEFAULT_JOIN_SEPARATOR;
        }

        StringBuilder joinStr = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            joinStr.append(list.get(i));
            if (i != list.size() - 1) {
                joinStr.append(separator);
            }
        }
        return joinStr.toString();
    }

    /**
     * add distinct entry to list
     * 
     * @param <V>
     * @param sourceList
     * @param entry
     * @return if entry already exist in sourceList, return false, else add it and return true.
     */
    public static <V> boolean addDistinctEntry(List<V> sourceList, V entry) {
        return (sourceList != null && !sourceList.contains(entry)) ? sourceList.add(entry) : false;
    }

    /**
     * add all distinct entry to list1 from list2
     * 
     * @param <V>
     * @param sourceList
     * @param entryList
     * @return the count of entries be added
     */
    public static <V> int addDistinctList(List<V> sourceList, List<V> entryList) {
        if (sourceList == null || isEmpty(entryList)) {
            return 0;
        }

        int sourceCount = sourceList.size();
        for (V entry : entryList) {
            if (!sourceList.contains(entry)) {
                sourceList.add(entry);
            }
        }
        return sourceList.size() - sourceCount;
    }

    /**
     * remove duplicate entries in list
     * 
     * @param <V>
     * @param sourceList
     * @return the count of entries be removed
     */
    public static <V> int distinctList(List<V> sourceList) {
        if (isEmpty(sourceList)) {
            return 0;
        }

        int sourceCount = sourceList.size();
        int sourceListSize = sourceList.size();
        for (int i = 0; i < sourceListSize; i++) {
            for (int j = (i + 1); j < sourceListSize; j++) {
                if (sourceList.get(i).equals(sourceList.get(j))) {
                    sourceList.remove(j);
                    sourceListSize = sourceList.size();
                    j--;
                }
            }
        }
        return sourceCount - sourceList.size();
    }

    /**
     * add not null entry to list
     * 
     * @param sourceList
     * @param value
     * @return <ul>
     *         <li>if sourceList is null, return false</li>
     *         <li>if value is null, return false</li>
     *         <li>return {@link List#add(Object)}</li>
     *         </ul>
     */
    public static <V> boolean addListNotNullValue(List<V> sourceList, V value) {
        return (sourceList != null && value != null) ? sourceList.add(value) : false;
    }
    
    /**
     * invert list
     * 
     * @param <V>
     * @param sourceList
     * @return
     */
    public static <V> List<V> invertList(List<V> sourceList) {
        if (isEmpty(sourceList)) {
            return sourceList;
        }

        List<V> invertList = new ArrayList<V>(sourceList.size());
        for (int i = sourceList.size() - 1; i >= 0; i--) {
            invertList.add(sourceList.get(i));
        }
        return invertList;
    }
    
    /**
     * 数组转List
     * @param array
     * @return
     */
    public static <T> ArrayList<T> convertArrayToList(T[] array){
        ArrayList<T> ret = new ArrayList<T>(array.length);
        for(T t : array){
            ret.add(t);
        }

        return ret;
    }
    
    /**
     * Creates an empty {@code ArrayList} instance.
     *
     * <p><b>Note:</b> if you only need an <i>immutable</i> empty List, use
     * {@link Collections#emptyList} instead.
     *
     * @return a newly-created, initially-empty {@code ArrayList}
     */
    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    /**
     * Creates a resizable {@code ArrayList} instance containing the given
     * elements.
     *
     * <p><b>Note:</b> due to a bug in javac 1.5.0_06, we cannot support the
     * following:
     *
     * <p>{@code List<Base> list = Lists.newArrayList(sub1, sub2);}
     *
     * <p>where {@code sub1} and {@code sub2} are references to subtypes of
     * {@code Base}, not of {@code Base} itself. To get around this, you must
     * use:
     *
     * <p>{@code List<Base> list = Lists.<Base>newArrayList(sub1, sub2);}
     *
     * @param elements the elements that the list should contain, in order
     * @return a newly-created {@code ArrayList} containing those elements
     */
    public static <E> ArrayList<E> newArrayList(E... elements) {
        int capacity = (elements.length * 110) / 100 + 5;
        ArrayList<E> list = new ArrayList<E>(capacity);
        Collections.addAll(list, elements);
        return list;
    }

    /** Clones a SparseArray. */
    public static <E> SparseArray<E> cloneSparseArray(SparseArray<E> orig) {
        SparseArray<E> result = new SparseArray<E>();
        for (int i = 0; i < orig.size(); i++) {
            result.put(orig.keyAt(i), orig.valueAt(i));
        }
        return result;
    }
}
