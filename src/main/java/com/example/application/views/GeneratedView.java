package com.example.application.views;

import com.example.application.service.WebsiteInfoService;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;

import jakarta.annotation.security.PermitAll;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@PermitAll
@Route("generated")
public class GeneratedView extends AppLayout implements HasUrlParameter<String> {
    private final WebsiteInfoService websiteInfoService;

    public GeneratedView(WebsiteInfoService websiteInfoService) {
    	this.websiteInfoService = websiteInfoService;
    }

    @Override
    public void setParameter(BeforeEvent event, String title) {
    	createHeader();
        createMainLayout(title);
    }

    private void createMainLayout(String title) {
    	VerticalLayout mainLayout = new VerticalLayout();
    	mainLayout.addClassName("generated-main-layout");

    	Span successfulText = new Span(title + "'s content summarized successfully.");
    	successfulText.addClassName("successful-text");

    	String localDateTime = LocalDateTime.now(ZoneId.of("Asia/Manila")).format(DateTimeFormatter.ofPattern("MM',' dd',' yyyy hh:mm a"));
    	Span timeText = new Span(new Icon("vaadin", "clock"), new Span("At " + localDateTime));
    	timeText.addClassName("time-text");

    	Span downloadInfoText = new Span(
    	   "You can view and read the summarized content by downloading the generated PDF file below. " +
    	   "The PDF file contains the summarized content made by our AI."
    	);
    	downloadInfoText.addClassName("download-info-text");

    	Span downloadNowText = new Span("Download now");

    	SvgIcon downloadPdfIcon = new SvgIcon(new StreamResource("pdf.svg", () -> getClass().getResourceAsStream("/META-INF/resources/icons/pdf.svg")));

    	Span generatedPdf = new Span(downloadPdfIcon, new Span("summarized-content.pdf"));

    	Button downloadButton = new Button("Download", new Icon("vaadin", "download"));
    	downloadButton.addClickListener(event -> {
    	    downloadButton.setText("Downloading...");
    	    downloadButton.setIcon(new Icon("vaadin", "circle"));
    	    simulateDelay();
    	});

    	Div downloadDiv = new Div(downloadNowText, generatedPdf, downloadButton);
    	downloadDiv.addClassName("download-div");

    	mainLayout.add(
    	    successfulText,
    	    timeText,
    	    downloadInfoText,
    	    downloadDiv,
    	    createRating()
    	);

    	setContent(mainLayout);
    }

    public void simulateDelay() {
        // Vaadin's access to ensure navigation occurs on the UI thread after the delay
        UI ui = UI.getCurrent();
        ui.access(() -> {
            // Start the delay using a Timer to avoid blocking the UI thread
            new java.util.Timer().schedule(new java.util.TimerTask() {
                @Override
                public void run() {
                    ui.access(() -> {
                        UI.getCurrent().navigate(MainView.class); // Safe navigation
                        Notification.show("PDF downloaded successfully", 3000, Notification.Position.TOP_CENTER)
                		.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    });
                }
            }, 5000); // Delay of 5 seconds
        });
    }

    private VerticalLayout createRating() {
    	Div starsLayout = new Div();
	starsLayout.addClassName("stars-layout");

    	String[] starIcons = {"star-o", "star-o", "star-o", "star-o", "star-o"};

    	for (String starIcon : starIcons) {
    	    Icon icon = new Icon("vaadin", starIcon);
    	    icon.addClickListener(event -> {
    	    	icon.getElement().setAttribute("icon", "vaadin:star");
    	    	icon.getStyle().set("color", "#0ef");
    	    });

    	    starsLayout.add(icon);
    	}

	Span rateText = new Span("Rate this summarization");

    	VerticalLayout ratingLayout = new VerticalLayout(rateText, starsLayout);
    	ratingLayout.addClassName("rating-layout");

    	return ratingLayout;
    }

    private void createHeader() {
    	Icon backIcon = new Icon("lumo", "arrow-left");
    	backIcon.addClickListener(event -> UI.getCurrent().navigate(MainView.class));

    	SvgIcon aiIcon = new SvgIcon(new StreamResource("ai.svg", () -> getClass().getResourceAsStream("/META-INF/resources/icons/ai.svg")));
        aiIcon.addClassName("ai-generated-icon");

        HorizontalLayout headerLayout = new HorizontalLayout(backIcon, new Span("Summarized content"), aiIcon);
        headerLayout.addClassName("generated-header");

        addToNavbar(headerLayout);
    }
}
