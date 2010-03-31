package com.feup.jhotsketch.connector;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Connector")
public interface ConnectorObserver {
	void connectorChanged();
}
