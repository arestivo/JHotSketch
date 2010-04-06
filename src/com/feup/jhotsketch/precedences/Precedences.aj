package com.feup.jhotsketch.precedences;

import com.feup.contribution.aida.annotations.PackageName;
import com.feup.jhotsketch.clipboard.EditMenu;
import com.feup.jhotsketch.persistency.FileMenu;
import com.feup.jhotsketch.preferences.PreferencesMenu;

@PackageName("Precedences")
public aspect Precedences {
	declare precedence : PreferencesMenu, EditMenu, FileMenu;
}
