package Model.Parsers.ParsingProcess;
import Model.DataObjects.ParseableObjects.ParseableObject;

/**
 * all Parsers must implements the parsing method
 */

public interface IParsingProcess {
    void parsing(ParseableObject parseableObject);
}
