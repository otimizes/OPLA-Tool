package arquitetura.helpers;

import org.apache.log4j.Logger;

import arquitetura.exceptions.ModelIncompleteException;
import arquitetura.exceptions.ModelNotFoundException;

/**
 * @author edipofederle<edipofederle@gmail.com>
 */
public class ModelHelperFactory {
	private static final Logger LOGGER = Logger.getLogger(ModelHelperFactory.class);

    private static ModelHelper instance;

    public static ModelHelper getModelHelper() {
        if (instance == null)
            try {
                instance = new ModelHelper();
            } catch (ModelNotFoundException e) {
            	LOGGER.error(e);
            	throw new RuntimeException();
            } catch (ModelIncompleteException e) {
            	LOGGER.error(e);
            	throw new RuntimeException();
            }

        return instance;
    }
}