package memory.indexer;

enum Token {

    LEFT("("), RIGHT(")");

    private final String value;

    Token(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}