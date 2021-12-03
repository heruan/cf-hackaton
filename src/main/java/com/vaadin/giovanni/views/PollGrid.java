package com.vaadin.giovanni.views;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.vaadin.collaborationengine.CollaborationEngine;
import com.vaadin.collaborationengine.CollaborationList;
import com.vaadin.collaborationengine.TopicConnection;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.shared.Registration;

public class PollGrid extends Composite<Grid<Option>> {

    private final UserInfo user;

    private TopicConnection connection;

    private CollaborationList options;

    private List<Option> dataProvider = new ArrayList<>();

    public PollGrid(String pollName, String userName) {
        user = new UserInfo(userName);
        CollaborationEngine.getInstance().openTopicConnection(this, pollName,
                user, this::onTopicConnection);
        getContent().addColumn(Option::getDescription);
        getContent().addComponentColumn(option -> new Button("Vote!", event -> {
            String optionId = option.getId();
            connection.getNamedList(optionId).append(user);
        }));
        getContent().addComponentColumn(option -> {
            String optionId = option.getId();
            CollaborationList votes = connection.getNamedList(optionId);
            Span voteCountSpan = new Span();
            votes.subscribe(event -> voteCountSpan.setText(
                    "Total votes: " + votes.getItems(UserInfo.class).size()));
            return voteCountSpan;
        });
        getContent().setItems(dataProvider);
    }

    public void addOption(String description) {
        options.append(new Option(UUID.randomUUID().toString(), description));
    }

    private Registration onTopicConnection(TopicConnection connection) {
        this.connection = connection;
        options = connection
                .getNamedList(PollGrid.class.getName() + ".options");
        options.subscribe(change -> {
            Option option = change.getValue(Option.class);
            dataProvider.add(option);
            getContent().getDataProvider().refreshAll();
        });
        return this::onDeactivate;
    }

    private void onDeactivate() {
        connection = null;
        options = null;
    }
}
