package memory.indexer;

class Item {

    private final Token token;

    private final String content;

    public Item(String content) {
        if (content == null) {
            throw new RuntimeException();
        }
        this.token = null;
        this.content = content;
    }

    public Item(Token token) {
        if (token == null) {
            throw new RuntimeException();
        }
        this.token = token;
        this.content = null;
    }

    public String getValue() {
        if (token == null) {
            return content;
        } else {
            return token.getValue();
        }
    }

    public boolean isLeftItem() {
        return token == Token.LEFT;
    }

    public boolean isRightItem() {
        return token == Token.RIGHT;
    }

    public static Item leftItem() {
        return new Item(Token.LEFT);
    }

    public static Item rightItem() {
        return new Item(Token.RIGHT);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Item)) {
            return false;
        }
        if (token == null) {
            return content.equals(((Item) obj).content);
        } else {
            return token == ((Item) obj).token;
        }
    }
}
