package com.vaadin.giovanni.views.helloworld;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.giovanni.views.MainLayout;
import com.vaadin.giovanni.views.PollGrid;

@PageTitle("CF Hackaton")
@Route(value = "hello", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class HelloWorldView extends VerticalLayout {

    public HelloWorldView() {
        setMargin(true);
        PollGrid pollComponent = new PollGrid("cf-hackaton", "Giovanni");
        HorizontalLayout layout = new HorizontalLayout();
        TextField newOptionField = new TextField("Add an option");
        layout.add(newOptionField);
        Button newOptionButton = new Button("Add to Poll", event -> {
            if (!newOptionField.isEmpty()) {
                String newOption = newOptionField.getValue();
                pollComponent.addOption(newOption);
                newOptionField.clear();
            }
        });
        layout.add(newOptionButton);
        newOptionField.addKeyUpListener(Key.ENTER,
                event -> newOptionButton.click());
        layout.setAlignItems(Alignment.BASELINE);
        add(layout, pollComponent);
    }

}
