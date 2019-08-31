package de.thm.noah.ruben.kantyapp.model;

public interface ValueKey {

    public static final String APP_DATA = "appData";
    public static final String NOTE_LIST = "notes";
    public static final String NOTE_ID = "noteID";
    public static final String NOTE_TEXT = "noteText";

// Markdown signs just before
    // Markdown Header
    public static final String H1 = "# ";
    public static final String H2 = "## ";
    public static final String H3 = "### ";
    public static final String H4 = "#### ";
    public static final String H5 = "##### ";

    // Lists
    public static final String UNORDERED  = "* ";

    //Miscellaneous
    public static final String BLOCKQUOTES  = "> ";


    // Own stuff
    public static final String TAG = "$";
    public static final String DATE = ">";

// Markdown signs before and after
    // Markdown Emphasis
    public static final String ITALIC = "*";
    public static final String BOLD = "__";
    public static final String STRIKETHROUGH  = "~~";

// Markdown signs after
    public static final String H_RULE  = "\n---\n";

}
