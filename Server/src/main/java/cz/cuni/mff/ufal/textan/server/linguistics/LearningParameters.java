package cz.cuni.mff.ufal.textan.server.linguistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * Created by Jakub Vlcek on 15.6.2014.
 */

/**
 * Class for read nametag training parameters.
 */
public class LearningParameters {
    private static final String TRAIN_NER = "train_ner";
    private static final String WAITING_TIME = "waiting_time";
    private static final String TRAINING_DATA = "default_training_data_file";
    private static final String USE_DEFAULT_TRAINING_DATA = "use_default_training_data";
    private static final String MAXIMUM_STORED_MODELS = "maximum_stored_models";
    private static final String FEATURES_FILE = "featuresFile";
    private static final String FORM = "form";
    private static final String LEMMA = "lemma";
    private static final String RAW_LEMMA = "raw_lemma";
    private static final String RAW_LEMMA_CAPITALIZATION = "raw_lemma_capitalization";
    private static final String TAG = "tag";
    private static final String NUMERIC_TIME_VALUE = "numeric_time_value";
    private static final String CZECH_LEMMA_TERM = "czech_lemma_term";
    private static final String BROWN_CLUSTERS = "brown_clusters";
    private static final String BROWN_CLUSTERS_FILE = "brown_clusters_file";
    private static final String GAZETTEERS = "gazetteers";
    private static final String GAZETTEERS_DIRECTORY = "gazetteers_directory";
    private static final String PREVIOUS_STAGE = "previous_stage";
    private static final String EMAIL_DETECTOR = "email_detector";

    private static final String DEFAULT_TRAINING_DATA_PATH = "cnec2.0-all" + File.separator + "train.txt";
    private static final int DEFAULT_WAITING_TIME = 300000;
    private static final int DEFAULT_MAXIMUM_STORED_MODELS = 5;
    private static final boolean DEFAULT_USE_DEFAULT_DATA = true;
    private static final String DEFAULT_NER_IDENTIFIER = "czech";
    private static final String DEFAULT_TAGGER = "morphodita:czech-131112-pos_only.tagger";
    private static final String DEFAULT_FEATURES_FILE = "features.txt";
    private static final String DEFAULT_STAGES = "2";
    private static final String DEFAULT_ITERATIONS = "30";
    private static final String DEFAULT_MISSING_WEIGHT = "-0.1";
    private static final String DEFAULT_INITIAL_LEARNING_RATE = "0.1";
    private static final String DEFAULT_FINAL_LEARNING_RATE = "0.01";
    private static final String DEFAULT_GAUSSIAN = "0.5";
    private static final String DEFAULT_HIDDEN_LAYER = "0";
    private static final String DEFAULT_HELDOUT_DATA = "";

    private static final int DEFAULT_FORM = 2;
    private static final int DEFAULT_LEMMA = 2;
    private static final int DEFAULT_RAW_LEMMA = 2;
    private static final int DEFAULT_RAW_LEMMA_CAPITALIZATION = 2;
    private static final int DEFAULT_TAG = 2;
    private static final int DEFAULT_NUMERIC_TIME_VALUE = 1;
    private static final int DEFAULT_CZECH_LEMMA_TERM = 2;
    private static final int DEFAULT_BROWN_CLUSTERS = 2;
    private static final String DEFAULT_BROWN_CLUSTERS_FILE = "clusters/wiki-1000-3";
    private static final int DEFAULT_GAZETTEERS = 2;
    private static final String DEFAULT_GAZETTEERS_DIRECTORY = "Gazetteers";
    private static final int DEFAULT_PREVIOUS_STAGE = 5;
    private static final String DEFAULT_EMAIL_DETECTOR = "mi me";

    private static final Logger LOG = LoggerFactory.getLogger(LearningParameters.class);

    private static final String[] DEFAULT_CONFIG_VALUES = { DEFAULT_NER_IDENTIFIER,
                                      DEFAULT_TAGGER,
                                      DEFAULT_FEATURES_FILE,
                                      DEFAULT_STAGES,
                                      DEFAULT_ITERATIONS,
                                      DEFAULT_MISSING_WEIGHT,
                                      DEFAULT_INITIAL_LEARNING_RATE,
                                      DEFAULT_FINAL_LEARNING_RATE,
                                      DEFAULT_GAUSSIAN,
                                      DEFAULT_HIDDEN_LAYER,
                                      DEFAULT_HELDOUT_DATA };
    private static final String[] LEARNING_PARAM_NAMES = {"ner_identifier", "tagger", "featuresFile", "stages", "iterations", "missing_weight", "initial_learning_rage", "final_learning_rage", "gaussian", "hidden_layer", "heldout_data"};

    private List<String> command;

    private File featuresFile;
    private int waitingTime;
    private int maximumStoredModels;
    private int form;
    private int lemma;
    private int rawLemma;
    private int rawLemmaCapitalization;
    private int tag;
    private int numericTimeValue;
    private int czechLemmaTerm;
    private int brownClusters;
    private File brownClustersFile;
    private int gazetteers;
    private File gazetteersDirectory;
    private int previousStage;
    private String URLEmailDetector;

    private File trainingDataFile;

    private boolean useDefaultTrainingData;

    /**
     * Read learning parameters for nametag
     * @param binaryDirectory directory, where learning binary is stored
     * @param dataDirectory directory, where
     * @param trainingDirectory directory, where training files are stored
     * */
    public LearningParameters(File binaryDirectory, File dataDirectory, File trainingDirectory) {
        command = new LinkedList<>();
        command.add(new File(binaryDirectory, mapBinaryName(TRAIN_NER)).toString());


        try (InputStream configFileStream = NamedEntityRecognizer.class.getResource("/NametagLearning.properties").openStream()){
            Properties p = new Properties();
            p.load(configFileStream);
            String value;
            for (int i = 0; i < LEARNING_PARAM_NAMES.length; ++i) {
                value = getStringProperty(p, LEARNING_PARAM_NAMES[i], DEFAULT_CONFIG_VALUES[i]);
                if (!value.isEmpty()) {
                    command.add(value);
                }
            }

            useDefaultTrainingData = getBooleanProperty(p, USE_DEFAULT_TRAINING_DATA, DEFAULT_USE_DEFAULT_DATA);

            if (useDefaultTrainingData) {
                try {
                    trainingDataFile = new File(dataDirectory.getAbsolutePath() + File.separator + getStringProperty(p, TRAINING_DATA, DEFAULT_TRAINING_DATA_PATH)).getCanonicalFile();
                } catch (IOException e) {
                    LOG.warn("Config value {} wasn't set, using default value.", TRAINING_DATA);
                } finally {
                    if (trainingDataFile == null) {
                        try {
                            trainingDataFile = new File(dataDirectory.getAbsolutePath() + File.separator + DEFAULT_TRAINING_DATA_PATH).getCanonicalFile();
                        } catch (IOException e) {
                            LOG.error("Default learning data file not exists.", e);
                        }
                    }
                }
            }

            waitingTime = getIntegerProperty(p, WAITING_TIME, DEFAULT_WAITING_TIME);

            maximumStoredModels = getIntegerProperty(p, MAXIMUM_STORED_MODELS, DEFAULT_MAXIMUM_STORED_MODELS);

            featuresFile = getFileProperty(p, FEATURES_FILE, trainingDirectory, DEFAULT_FEATURES_FILE);
            form = getIntegerProperty(p, FORM, DEFAULT_FORM);
            lemma = getIntegerProperty(p, LEMMA, DEFAULT_LEMMA);
            rawLemma = getIntegerProperty(p, RAW_LEMMA, DEFAULT_RAW_LEMMA);
            rawLemmaCapitalization = getIntegerProperty(p, RAW_LEMMA_CAPITALIZATION, DEFAULT_RAW_LEMMA_CAPITALIZATION);
            tag = getIntegerProperty(p, TAG, DEFAULT_TAG);
            numericTimeValue = getIntegerProperty(p, NUMERIC_TIME_VALUE, DEFAULT_NUMERIC_TIME_VALUE);
            czechLemmaTerm = getIntegerProperty(p, CZECH_LEMMA_TERM, DEFAULT_CZECH_LEMMA_TERM);
            brownClusters = getIntegerProperty(p, BROWN_CLUSTERS, DEFAULT_BROWN_CLUSTERS);
            brownClustersFile = getFileProperty(p, BROWN_CLUSTERS_FILE, trainingDirectory, DEFAULT_BROWN_CLUSTERS_FILE);
            gazetteers = getIntegerProperty(p, GAZETTEERS, DEFAULT_GAZETTEERS);
            gazetteersDirectory = getFileProperty(p, GAZETTEERS_DIRECTORY, trainingDirectory, DEFAULT_GAZETTEERS_DIRECTORY);
            previousStage = getIntegerProperty(p, PREVIOUS_STAGE, DEFAULT_PREVIOUS_STAGE);
            URLEmailDetector = getStringProperty(p, EMAIL_DETECTOR, DEFAULT_EMAIL_DETECTOR);

        } catch (Exception e) {
            LOG.warn("Config file for NameTag wasn't found, using default values.", e.getMessage());
            for (int i = 0; i < LEARNING_PARAM_NAMES.length; ++i) {
                if (!DEFAULT_CONFIG_VALUES[i].isEmpty()) {
                    command.add(DEFAULT_CONFIG_VALUES[i]);
                }
            }

            try {
                trainingDataFile = new File(dataDirectory.getAbsolutePath() + File.separator + DEFAULT_TRAINING_DATA_PATH).getCanonicalFile();
            }
            catch (IOException ioe) {
                LOG.warn("Config value {} wasn't set, using default value.", TRAINING_DATA);
            } finally {
                if (trainingDataFile == null) {
                    try {
                        trainingDataFile = new File(dataDirectory.getAbsolutePath() + File.separator + DEFAULT_TRAINING_DATA_PATH).getCanonicalFile();
                    } catch (IOException ioe) {
                        LOG.error("Default learning data file not exists.", ioe);
                    }
                }
            }

            waitingTime = DEFAULT_WAITING_TIME;
            maximumStoredModels = DEFAULT_MAXIMUM_STORED_MODELS;
            featuresFile = new File(trainingDirectory + DEFAULT_FEATURES_FILE);
            form = DEFAULT_FORM;
            lemma = DEFAULT_LEMMA;
            rawLemma = DEFAULT_RAW_LEMMA;
            rawLemmaCapitalization = DEFAULT_RAW_LEMMA_CAPITALIZATION;
            tag = DEFAULT_TAG;
            numericTimeValue = DEFAULT_NUMERIC_TIME_VALUE;
            czechLemmaTerm = DEFAULT_CZECH_LEMMA_TERM;
            brownClusters = DEFAULT_BROWN_CLUSTERS;
            brownClustersFile = new File(trainingDirectory + DEFAULT_BROWN_CLUSTERS_FILE);
            gazetteers = DEFAULT_GAZETTEERS;
            gazetteersDirectory = new File(trainingDirectory + DEFAULT_GAZETTEERS_DIRECTORY);
            previousStage = DEFAULT_PREVIOUS_STAGE;
            URLEmailDetector = DEFAULT_EMAIL_DETECTOR;
        }
    }

    /**
     * Return string property from provided properties
     * @param p source properties
     * @param propertyName property name
     * @param defaultValue default value
     * @return property value if property is set, default value else
     */
    private String getStringProperty(Properties p, String propertyName, String defaultValue) {
        String value = defaultValue;
        try {
            value = (String) p.get(propertyName);
            if (value == null) {
                LOG.warn("Config value {} wasn't set, using default value {}.", propertyName, defaultValue);
                value = defaultValue;
            }
        } catch (Exception e) {
            LOG.warn("Config value {} wasn't set, using default value {}.", propertyName, defaultValue);
        }
        return value;
    }

    /**
     * Return boolean property from provided properties
     * @param p source properties
     * @param propertyName property name
     * @param defaultValue default value
     * @return property value if property is set, default value else
     */
    private boolean getBooleanProperty(Properties p, String propertyName, boolean defaultValue) {
        Boolean value = defaultValue;
        try {
            value = Boolean.valueOf((String) p.get(propertyName));
            if (value == null) {
                LOG.warn("Config value {} wasn't set, using default value {}.", propertyName, defaultValue);
                value = defaultValue;
            }
        } catch (Exception e) {
            LOG.warn("Config value {} wasn't set, using default value {}.", propertyName, defaultValue);
        }
        return value;
    }

    private File getFileProperty(Properties p, String propertyName, File directory,String defaultFilename) {
        File value = null;
        try {
            value = new File(directory.getAbsolutePath() + File.separator + getStringProperty(p, propertyName, defaultFilename)).getCanonicalFile();
        } catch (IOException e) {
            LOG.warn("Config value {} wasn't set, using default value.", propertyName);
        } finally {
            if (value == null) {
                try {
                    value = new File(directory.getAbsolutePath() + File.separator + defaultFilename).getCanonicalFile();
                } catch (IOException e) {
                    LOG.error("Default {} = {} not exists.", propertyName, defaultFilename, e);
                }
            }
        }
        return  value;
    }

    /**
     * Return integer property from provided properties
     * @param p source properties
     * @param propertyName property name
     * @param defaultValue default value
     * @return property value if property is set, default value else
     */
    private int getIntegerProperty(Properties p, String propertyName, int defaultValue) {
        int value = defaultValue;
        try {
            value = Integer.parseInt((String)p.get(propertyName));
            LOG.warn("Config value {} wasn't set, using default value {}.", propertyName, defaultValue);
        }
        catch (NumberFormatException nfe) {
            value = defaultValue;
            LOG.warn("Could not parse config value {}, using default value {}.", propertyName, defaultValue);
        }
        catch (Exception e) {
            value = defaultValue;
            LOG.warn("Config value {} wasn't set, using default value {}.", propertyName, defaultValue);
        }
        return value;
    }

    /**
     * @return list of command line arguments
     */
    public List<String> getCommand() {
        return command;
    }

    /**
     * @return maximum waiting time for learning
     */
    public int getWaitingTime() {
        return waitingTime;
    }

    /**
     * @return maximum stored models on disk
     */
    public int getMaximumStoredModels() {
        return maximumStoredModels;
    }

    /**
     * @return true if default training data should be used
     */
    public boolean useDefaultTrainingData() {
        return useDefaultTrainingData;
    }

    /**
     * @return file with training data
     */
    public File getTrainingData() {
        return trainingDataFile;
    }

    public File getFeatruresFile() {
        return featuresFile;
    }

    public int getForm() {
        return form;
    }

    public int getLemma() {
        return lemma;
    }

    public int getRawLemma() {
        return rawLemma;
    }

    public int getRawLemmaCapitalization() {
        return rawLemmaCapitalization;
    }

    public int getTag() {
        return tag;
    }

    public int getNumericTimeValue() {
        return numericTimeValue;
    }

    public int getCzechLemmaTerm() {
        return czechLemmaTerm;
    }

    public int getBrownClusters() {
        return brownClusters;
    }

    public File getBrownClustersFile() {
        return brownClustersFile;
    }

    public int getGazetteers() {
        return gazetteers;
    }

    public File getGazetteersDirectory() {
        return gazetteersDirectory;
    }

    public int getPreviousStage() {
        return previousStage;
    }

    public String getURLEmailDetector() {
        return URLEmailDetector;
    }

    /**
     * map binary name based on OS
     * @param binName input binary name
     * @return mapped binary name
     */
    private static String mapBinaryName(String binName) {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return binName + ".exe";
        } else {
            return binName;
        }
    }



}
