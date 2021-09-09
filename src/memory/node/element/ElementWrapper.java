package memory.node.element;

public interface ElementWrapper extends Element {

    Element getElement();

    <T extends Element> T getElement(Class<T> clazz);

    void setElement(Element element);

}
