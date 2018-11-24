package Model.Parsers;
import Model.DataObjects.ParseableObjects.ParseableObject;

/**
 * all Parsers must implements the parsing method
 */

public interface IParser {
    void parsing(ParseableObject parseableObject);
}
