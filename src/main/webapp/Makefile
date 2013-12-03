BOOTSTRAP = ./docs/assets/css/bootstrap.css
BOOTSTRAP_LESS = ./less/bootstrap.less
BOOTSTRAP_RESPONSIVE = ./docs/assets/css/bootstrap-responsive.css
BOOTSTRAP_RESPONSIVE_LESS = ./less/responsive.less

#
# CLEANS THE ROOT DIRECTORY OF PRIOR BUILDS
#

clean:
	rm -f css/bootstrap.css
	rm -f css/bootstrap.min.css
	rm -f css/bootstrap-responsive.css
	rm -f css/bootstrap-responsive.min.css
	rm -f js/bootstrap.js
	rm -f js/bootstrap.min.js

#
# BUILD SIMPLE BOOTSTRAP DIRECTORY
# recess & uglifyjs are required
#

bootstrap:
	recess --compile ${BOOTSTRAP_LESS} > css/bootstrap.css
	recess --compress ${BOOTSTRAP_LESS} > css/bootstrap.min.css
	recess --compile ${BOOTSTRAP_RESPONSIVE_LESS} > css/bootstrap-responsive.css
	recess --compress ${BOOTSTRAP_RESPONSIVE_LESS} > css/bootstrap-responsive.min.css
	cat js/bootstrap-transition.js js/bootstrap-alert.js js/bootstrap-button.js js/bootstrap-carousel.js js/bootstrap-collapse.js js/bootstrap-dropdown.js js/bootstrap-modal.js js/bootstrap-tooltip.js js/bootstrap-popover.js js/bootstrap-scrollspy.js js/bootstrap-tab.js js/bootstrap-typeahead.js js/bootstrap-affix.js > js/bootstrap.js
	uglifyjs -nc js/bootstrap.js > js/bootstrap.min.tmp.js
	echo "/*!\n* Bootstrap.js by @fat & @mdo\n* Copyright 2012 Twitter, Inc.\n* http://www.apache.org/licenses/LICENSE-2.0.txt\n*/" > js/copyright.js
	cat js/copyright.js js/bootstrap.min.tmp.js > js/bootstrap.min.js
	rm js/copyright.js js/bootstrap.min.tmp.js

#
# WATCH LESS FILES
#

watch:
	echo "Watching less files..."; \
	watchr -e "watch('less/.*\.less') { system 'make clean'; system 'make bootstrap' }"


.PHONY: docs watch
