
package app.models;

import org.javalite.activejdbc.Model;

/**
 * Let the competition begin!
 */
public class Contest extends Model {
    static {
        validatePresenceOf("name").message("Name is required");
        validatePresenceOf("number_of_prizes").message("Number of prices is required");;
        // validatePresenceOf("author").message("Author must be provided");
    }

}