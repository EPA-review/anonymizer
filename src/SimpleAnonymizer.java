import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sun.javaws.jnl.LaunchSelection;
import com.swabunga.spell.engine.SpellDictionary;
import com.swabunga.spell.engine.SpellDictionaryHashMap;
import com.swabunga.spell.engine.Word;
import com.swabunga.spell.event.SpellCheckEvent;
import com.swabunga.spell.event.SpellCheckListener;
import com.swabunga.spell.event.SpellChecker;
import com.swabunga.spell.event.StringWordTokenizer;
import edu.washington.cs.knowitall.morpha.MorphaStemmer;


public class SimpleAnonymizer implements SpellCheckListener{

    protected Boolean testing = true;

    protected ArrayList<String> _nicknames;
    protected SpellChecker spellCheck = null;
    protected SpellDictionary dictionary = null;
    protected ArrayList<String> misspelled;


    private String AnonymizeWord(String W, String FirstNameR, String LastNameR, String FirstNameO, String LastNameO, Boolean F) {
        String result = W;

        result = AnonymizeWord_Pronoun(result);
        result = AnonymizeWord_Adjective(result);
        result = AnonymizeWord_Abbreviations(result);
        result = AnonymizeWord_Name(W, FirstNameR, LastNameR);
        result = AnonymizeWord_Name(W, FirstNameO, LastNameO);
        result = AnonymizeWord_NickName(W, FirstNameR, LastNameR);
        result = AnonymizeWord_NickName(W, FirstNameO, LastNameO);
        result = AnonymizeWord_NameSubword(W, FirstNameR);
        result = AnonymizeWord_NameSubword(W, LastNameR);
        result = AnonymizeWord_NameSubword(W, FirstNameO);
        result = AnonymizeWord_NameSubword(W, LastNameO);

        result = AnonmizeWord_Flag(W, F);

        return result;
    }

    private String AnonymizeWord_Adjective(String W){
        String result = W;

        if (W.equals("male")  ||
            W.equals("female")
        )
        {
            result = "ADJECTIVE";
        }

        return result;
    }

    private String AnonymizeWord_Abbreviations(String W){
        String result = W;

        if (W.equals("m")  ||
            W.equals("f")
        )
        {
            result = "FLAGGED";
        }

        return result;
    }

    private String AnonymizeWord_Pronoun(String W){
        String result = W;

        if (W.equals("he")  ||
            W.equals("she") ||
            W.equals("him") ||
            W.equals("her") ||
            W.equals("his") ||
            W.equals("hers") ||
            W.equals("himself") ||
            W.equals("herself") ||
            W.equals("man") ||
            W.equals("woman") ||
            W.equals("men") ||
            W.equals("women") ||
            W.equals("boy") ||
            W.equals("girl") ||
            W.equals("mother") ||
            W.equals("father") ||
            W.equals("lady") ||
            W.equals("uncle") ||
            W.equals("aunt") ||
            W.equals("son") ||
            W.equals("daughter") ||
            W.equals("husband") ||
            W.equals("wife")
        )
        {
            result = "PRONOUN";
        }

        return result;
    }

    private String AnonymizeWord_Name(String W, String FirstName, String LastName){
        String result = W;

        if (W.equals(FirstName)){
            result = "FIRSTNAME";
        }
        else if (W.equals(LastName)){
            result = "LASTNAME";
        }

        return result;
    }

    private String AnonymizeWord_NickName(String W, String FirstName, String LastName){
        String result = W;

        // should make come from a database probably

        if (_nicknames.contains(FirstName))
        {
            result = "NICKNAME";
        }
        else if (_nicknames.contains(LastName))
        {
            result = "NICKNAME";
        }

        return result;
    }

    private String AnonymizeWord_NameSubword(String W, String Name){
        String result = W;
        String processing = W;

        Boolean flagDone = false; // indicates if processing should end
        Boolean flagPrefix = false; // track if the prefix matches a possible name/nickname
        Boolean flagSpelling = false; // tracks if the word is misspelled
        Boolean flagNotWord = false; // tracks if the word is not recognized at all even as an English word
        Boolean flagSuffix = false; // tracks if the suffix is in general odd

        // does the word end in Y, I, K, etc.?
        Boolean flagSuffixY = false;
        Boolean flagSuffixI = false;
        Boolean flagSuffixK = false;
        Boolean flagSuffixH = false;
        Boolean flagSuffixIE = false;
        Boolean flagSuffixCH = false;
        Boolean flagSuffixKY = false;
        Boolean flagSuffixHY = false;

        misspelled = new ArrayList<String>();
        spellCheck.checkSpelling(new StringWordTokenizer(W));

        if (misspelled.size() > 0) {
            for (int iError = 0; iError < misspelled.size(); iError++) {
                List<Word> suggestions = spellCheck.getSuggestions(misspelled.get(iError), 1);
                if (suggestions.size() == 0) {
                    flagNotWord = true;
                }
                else{
                    flagSpelling = true;
                }
            }
        }

        // if the name starts with the word it is not a word
        if (Name.startsWith(W) && flagNotWord){
            result = "NICKNAME?";
            flagPrefix = true;
            flagDone = true;
        }

        // if the name starts with the word
        if (Name.startsWith(W)){
            result = "FLAGGED";
            flagPrefix = true;
            flagDone = true;
        }

        if(!flagDone){
            // does the word end in y or i or ie
            // if so strip it off
            if (processing.endsWith("y")){
                flagSuffixY = true;
                flagSuffix = true;
                processing = processing.substring(0,processing.length()-1);
            }
            else if (processing.endsWith("i")){
                flagSuffixI = true;
                flagSuffix = true;
                processing = processing.substring(0,processing.length()-1);
            }
            else if (processing.endsWith("k")){
                flagSuffixK = true;
                flagSuffix = true;
                processing = processing.substring(0,processing.length()-1);
            }
            else if (processing.endsWith("h")){
                flagSuffixH = true;
                flagSuffix = true;
                processing = processing.substring(0,processing.length()-1);
            }

            if (W.startsWith(processing)){
                result = "NICKNAME?";
                flagPrefix = true;
            }

            // if this is not already flagged, then take a look at strange two letter endings
            // reset the word
            processing = W;
            if (!flagPrefix){
                if (processing.endsWith("ie")){
                    flagSuffixIE = true;
                    flagSuffix = true;
                    processing = processing.substring(0,processing.length()-2);
                }
                else if (processing.endsWith("ch")){
                    flagSuffixCH = true;
                    flagSuffix = true;
                    processing = processing.substring(0,processing.length()-2);
                }
                else if (processing.endsWith("ky")){
                    flagSuffixKY = true;
                    flagSuffix = true;
                    processing = processing.substring(0,processing.length()-2);
                }
                else if (processing.endsWith("hy")){
                    flagSuffixHY = true;
                    flagSuffix = true;
                    processing = processing.substring(0,processing.length()-2);
                }

                // if after removing the ending the word matches the
                if (W.startsWith(processing)){
                    result = "NICKNAME?";
                    flagPrefix = true;
                }
            }

            // this word has a strange suffix, flag it
            if (!flagPrefix && (flagSuffixIE || flagSuffixI || flagSuffixKY)){
                result = "FLAGGED";
            }

            // this isn't a word and it has a strange ending
            if (flagNotWord && flagSuffix){
                result = "FLAGGED";
            }
        }

        return result;
    }

    private String AnonmizeWord_Flag(String W, Boolean F){
        String result = W;

        if (F){
            result = "FLAGGED";
        }

        return result;
    }



    private Boolean SetAnonFlag(String W){
        Boolean result = false;

        if (W.equals("doctor") ||
            W.equals("dr") ||
            W.equals("mister") ||
            W.equals("miss") ||
            W.equals("mr") ||
            W.equals("ms") ||
            W.equals("mrs")
           ){
            return true;
        }

        return result;
    }

    public String Anonymize(ArrayList<String> I){
        String output = "";
        ArrayList<String> sample = I;

        String resident_name = I.get(0);
        String observer_name = I.get(1);
        String feedback = I.get(2);
        String professionalism_safety = I.get(3);

        String[] nameTokens = resident_name.split("\\s");
        String firstnameR = nameTokens[0].toLowerCase();
        String lastnameR = nameTokens[nameTokens.length-1].toLowerCase();

        nameTokens = observer_name.split("\\s");
        String firstnameO = nameTokens[0].toLowerCase();
        String lastnameO = nameTokens[nameTokens.length-1].toLowerCase();

        System.out.println(resident_name + " = " + firstnameR + ", " + lastnameR);
        System.out.println(observer_name + " = " + firstnameO + ", " + lastnameO);

        String[] feedbackTokens = feedback.split("\\s");
        String[] prosafeTokens = professionalism_safety.split("\\s");

        ArrayList<String> anonFeedback = new ArrayList<>();
        ArrayList<String> anonProSafe = new ArrayList<>();

        Boolean flagAnon = false;

        for (int iToken = 0; iToken < feedbackTokens.length; iToken++){
            feedbackTokens[iToken] = feedbackTokens[iToken].replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}]", "");
            feedbackTokens[iToken] = new MorphaStemmer().morpha(feedbackTokens[iToken], false);
            anonFeedback.add(AnonymizeWord(feedbackTokens[iToken], firstnameR, lastnameR, firstnameO, lastnameO, flagAnon));
            flagAnon = SetAnonFlag(feedbackTokens[iToken]);
        }

        for (int iToken = 0; iToken < prosafeTokens.length; iToken++){
            prosafeTokens[iToken] = prosafeTokens[iToken].replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}]", "");
            prosafeTokens[iToken] = new MorphaStemmer().morpha(prosafeTokens[iToken], false);
            anonProSafe.add(AnonymizeWord(prosafeTokens[iToken], firstnameR, lastnameR, firstnameO, lastnameO, flagAnon));
            flagAnon = SetAnonFlag(prosafeTokens[iToken]);
        }

        System.out.println("Feedback");
        System.out.println("========");

        for (int iToken = 0; iToken < feedbackTokens.length; iToken++){
            System.out.println(feedbackTokens[iToken] + " -> " + anonFeedback.get(iToken));
        }

        System.out.println("");
        System.out.println("Professional & Safety");
        System.out.println("=====================");

        for (int iToken = 0; iToken < prosafeTokens.length; iToken++){
            System.out.println(prosafeTokens[iToken] + " -> " + anonProSafe.get(iToken));
        }

        return output;
    }

    protected void InitializeDictionary()
    {
        // initialize spell checking
        try {
            //String dictFileName = "./dict/eng_com.dic";
            String dictFileName = "./dict/english.0";
            String phonetFileName = "./dict/phonet.en";

            File dictFile = new File(dictFileName);
            File phonetFile = new File(phonetFileName);

            dictionary = new SpellDictionaryHashMap(dictFile, phonetFile);
            spellCheck = new SpellChecker(dictionary);
            spellCheck.addSpellCheckListener(this);

            // add all words in the master answer key to the dictionary
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean ValidateDictionary()
    {// currently requires no validation
        boolean result = true;
        return result;
    }

    public void spellingError(SpellCheckEvent event)
    {
        event.ignoreWord(true);
        misspelled.add(event.getInvalidWord());
    }


    public SimpleAnonymizer(ArrayList<String> Nicknames)
    {
        _nicknames = Nicknames;

        InitializeDictionary();
        try{
            if (!ValidateDictionary())
            {
                throw new Exception("Invalid dictionary");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void Test(){
        // ensure that all test cases are lowercase
    }
}
