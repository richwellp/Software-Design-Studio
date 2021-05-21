public class FinalOutputClassLoader extends ClassLoader {

    public FinalOutputPrinter loadFinalOutputPrinter() {
        byte[] classToLoad = FinalOutputByteHolder.finalOutputClassBytes;
        Class loadedClass = this.defineClass("FinalOutput", classToLoad, 0, classToLoad.length);

        try {
            return (FinalOutputPrinter) loadedClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.err.println("This really should never happen, please contact CS 126 course staff if you see this");
            e.printStackTrace();
        }

        return null;
    }
}
