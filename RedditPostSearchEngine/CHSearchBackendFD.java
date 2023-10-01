import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class CHSearchBackendFD implements CHSearchBackendInterface {
    public CHSearchBackendFD(HashtableWithDuplicateKeysInterface<String,PostInterface> hashtable, PostReaderInterface postReader){

    }
    @Override
    public void loadData(String filename) throws FileNotFoundException {
        if(filename.equals("illegalfile")){
            throw new FileNotFoundException("Error, file not found");
        }
        System.out.println("This file contains multiple reddit posts about cute cats");
    }

    @Override
    public List<String> findPostsByTitleWords(String words) {
        List<String> returnVal = new ArrayList<>();
        returnVal.add("post with "+ words);

        return returnVal;
    }

    @Override
    public List<String> findPostsByBodyWords(String words) {
        List<String> returnVal = new ArrayList<>();
        returnVal.add("post with body words: "+ words);
        return returnVal;
    }

    @Override
    public List<String> findPostsByTitleOrBodyWords(String words) {
        List<String> returnVal = new ArrayList<>();
        System.out.println("Now searching for titles with the terms...");
        returnVal.addAll(findPostsByTitleWords(words));
        System.out.println("Now searching for body with the terms...");
        returnVal.addAll(findPostsByBodyWords(words));
        return returnVal;
    }

    @Override
    public String getStatisticsString() {
        return "90% reddit users are still living in their mom's basement!";
    }
}
