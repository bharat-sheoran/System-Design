import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

interface DocumentElement {
    public String render();
}

class TextElement implements DocumentElement {
    private String text;

    public TextElement(String text){
        this.text = text;
    }

    @Override
    public String render() {
        return text;
    }
    
}

class ImageElement implements DocumentElement {
    private String imagePath;

    public ImageElement(String imagePath){
        this.imagePath = imagePath;
    }

    @Override
    public String render() {
        return "[Image: " + imagePath + "]";
    }
}

class NewLineElement implements DocumentElement{
    public String render(){
        return "\n";
    }
}

class TabSpaceElement implements DocumentElement{
    public String render(){
        return "\t";
    }
}

class Document {
    private ArrayList<DocumentElement> documentElements;

    Document(){
        documentElements = new ArrayList<>();
    }

    void addText(DocumentElement element) {
        documentElements.add(element);
    }

    String render(){
        String result = "";
        for(DocumentElement element: documentElements){
            result += element.render();
        }
        return result;
    }
}

interface Persistance {
    void saveToFile(String data);
}

class FileSave implements Persistance{
    @Override
    public void saveToFile(String data) {
        try (FileWriter writer = new FileWriter("output.txt")) {
            writer.write(data);
            System.out.println("File saved successfully to: " + "output.txt");
        } catch (IOException e) {
            System.err.println("Failed to save file: " + e.getMessage());
            e.printStackTrace();
        }
    } 
}

class DBSave implements Persistance {
    @Override
    public void saveToFile(String data) {
        System.out.println("File Saved to DB");
    }
}

class DocumentEditorApp {
    private Document document;
    private Persistance storage;
    String renderedDocument;

    DocumentEditorApp(Document document, Persistance storage){
        this.document = document;
        this.storage = storage;
        renderedDocument = "";
    }

    void addText(String text){
        this.document.addText(new TextElement(text));
    }

    void addImage(String imagePath){
        this.document.addText(new ImageElement(imagePath));
    }

    void addNewLine(){
        this.document.addText(new NewLineElement());
    }

    String renderDocument(){
        if(renderedDocument.isEmpty()){
            renderedDocument = this.document.render();
        }
        return renderedDocument;
    }

    void saveDocument() {
        this.storage.saveToFile(renderDocument());
    }
}

class DocumentEditor {
    public static void main(String[] args) {
        Document doc = new Document();
        Persistance editor = new FileSave();
        DocumentEditorApp documentEditor = new DocumentEditorApp(doc, editor);
        documentEditor.addText("This is 1st Text");
        documentEditor.addNewLine();
        documentEditor.addImage("image.png");
        documentEditor.addText("This is 2nd Text");

        System.out.println(documentEditor.renderDocument());
        documentEditor.saveDocument();
    }
}