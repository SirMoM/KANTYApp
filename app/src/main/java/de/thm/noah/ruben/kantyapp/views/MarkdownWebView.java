package de.thm.noah.ruben.kantyapp.views;

import android.content.Context;
import android.webkit.WebView;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

/**
 * @author Noah Ruben
 *
 * Meine Implementation einer MarkdownView die Markdown in HTML umwandelt hier könnten auch eigene Tokens eingespeist werden die auch umgewandelt werden.
 *
 */
public class MarkdownWebView extends WebView {

    public MarkdownWebView(Context context, String contendAsMarkdown) {
        super(context);
        loadContend(contendAsMarkdown);
    }

    /**
     * Benutzt den {@link Parser} um "Markdown" in "Tokens" aufzuteilen, diese werden mit dem  {@link HtmlRenderer} in HTML umgewandelt welches die WebView darstellen kann.
     *
     * @param contend der "Markdown"-Text der in der WebView dargestellt wird.
     */
    private void loadContend(String contend) {
        List<Extension> extensions = Arrays.asList(StrikethroughExtension.create(), AutolinkExtension.create(), TablesExtension.create());
        Parser parser = Parser.builder().extensions(extensions).build();
        Node document = parser.parse(contend + " \n___\n");
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
        String contendAsHTML = renderer.render(document);
        System.out.println("contendAsHTML = " + contendAsHTML);
        this.loadData(contendAsHTML,"text/html", "UFT-8");
    }
}
