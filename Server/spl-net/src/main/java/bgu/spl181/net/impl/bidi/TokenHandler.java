package bgu.spl181.net.impl.bidi;

import java.util.ArrayDeque;

public class TokenHandler {

    private String token;
    private Boolean hasNext;
    private ArrayDeque<String> tokens;
    private int numOfElments;


    public TokenHandler(String token){
        this.token=token;
        this.hasNext=!(token.isEmpty());
        this.tokens = new ArrayDeque<>();
        numOfElments=0;
    }

    public ArrayDeque<String>  Tokenize() {
        if(!hasNext) return null;

        while (hasNext) {
            int i = token.indexOf(" ");
            if (i == -1) {
                hasNext = false;
                tokens.add(token);
                numOfElments++;
            }else {
                tokens.add(token.substring(0, i));
                numOfElments++;
                token = token.substring(i + 1);
            }
        }

        return tokens;
    }

    public int size(){return numOfElments;}
}
