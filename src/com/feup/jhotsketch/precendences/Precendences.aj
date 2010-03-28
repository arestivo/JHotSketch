package com.feup.jhotsketch.precendences;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.groups.GroupMenu;
import com.feup.jhotsketch.groups.GroupToolbar;
import com.feup.jhotsketch.file.FileMenu;
import com.feup.jhotsketch.copypaste.CopyPasteMenu;
import com.feup.jhotsketch.copypaste.CopyPasteToolbar;
import com.feup.jhotsketch.snap.SnapToolbar;

@PackageName("Precedences")
public aspect Precendences {
	  declare precedence: GroupMenu , CopyPasteMenu , FileMenu, 
	                      SnapToolbar, OrderingToolbar, GroupToolbar , CopyPasteToolbar;
}
