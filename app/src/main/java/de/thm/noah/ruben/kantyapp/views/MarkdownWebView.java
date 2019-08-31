package de.thm.noah.ruben.kantyapp.views;

import android.content.Context;
import android.webkit.WebView;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownWebView extends WebView {

    public MarkdownWebView(Context context, String contendAsMarkdown) {
        super(context);
        loadContend(contendAsMarkdown);
    }

    private void loadContend(String contend) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(contend + " \n___\n");
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String contendAsHTML = renderer.render(document);
        this.loadData(contendAsHTML,"text/html", "UFT-8");
    }
}
