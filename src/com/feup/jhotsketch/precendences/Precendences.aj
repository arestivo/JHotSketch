package com.feup.jhotsketch.precendences;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.groups.GroupMenu;
import com.feup.jhotsketch.file.FileMenu;
import com.feup.jhotsketch.copypaste.CopyPasteMenu;

@PackageName("Precedences")
public aspect Precendences {
	  declare precedence: GroupMenu , CopyPasteMenu , FileMenu;
}
