<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-us" lang="en-us">
<head>
	<g:render template="/layouts/shared/headContent"/>
	<r:require module="common" />
	<r:layoutResources/>
</head>
<body class="not-authenticated">
<div class="site-wrapper">
	<!-- start header -->
	<header id="page-header">
		<nav class="top-bar">
			<div class="row">
				<div class="large-12 columns">
					<g:render template="/layouts/shared/topBarContent"/>
					<div id="logo"></div>
				</div>
			</div>
		</nav>
	</header>
	<!-- end header -->
	<div id="menu-content-wrapper" class="row">
		<div class="medium-12 columns">
			<div id="main">
				<div class="row">
					<g:layoutBody/>
				</div>
				<!-- start footer -->
				<footer id="page-footer">
					<g:render template="/layouts/shared/footer"/>
				</footer>
				<!-- end footer -->
			</div>
		</div>
	</div>
</div>
<r:layoutResources/>
</body>
</html>
