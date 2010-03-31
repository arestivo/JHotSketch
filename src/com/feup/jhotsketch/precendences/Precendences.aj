package com.feup.jhotsketch.precendences;

import com.feup.contribution.aida.annotations.PackageName; 
import com.feup.jhotsketch.groups.GroupMenu;
import com.feup.jhotsketch.groups.GroupToolbar;
import com.feup.jhotsketch.file.FileMenu;
import com.feup.jhotsketch.copypaste.CopyPasteMenu;
import com.feup.jhotsketch.copypaste.CopyPasteToolbar;
import com.feup.jhotsketch.ordering.OrderingToolbar;
import com.feup.jhotsketch.snap.SnapToolbar;
import com.feup.jhotsketch.properties.connector.ConnectorProperties;
import com.feup.jhotsketch.properties.shape.ShapeProperties;

@PackageName("Precedences")
public aspect Precendences {
	  declare precedence: GroupMenu , CopyPasteMenu , FileMenu, 
	                      SnapToolbar, OrderingToolbar, GroupToolbar , CopyPasteToolbar,
	                      ConnectorProperties, ShapeProperties;
}
