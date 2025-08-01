package ci.ada.adapter;

//import com.sun.tools.javac.util.List;

import java.util.List;


public interface DataReader<T> {
    List<T> getData();
}
