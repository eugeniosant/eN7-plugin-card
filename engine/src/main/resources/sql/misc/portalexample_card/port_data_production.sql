INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('card_list', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Personal Cards - List</property>
<property key="it">Personal Card - Lista</property>
</properties>

', NULL, NULL, NULL, NULL, 1, 'free');
INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('card_list_detail', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Personal Cards - List and Detail</property>
<property key="it">Personal Card - Lista e Dettagio</property>
</properties>', NULL, NULL, 'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/FrontEnd/Card/list.action</property>
</properties>', 1, 'free');




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('portalexample_card_is_cardFinding', 'card_list_detail', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<#assign s=JspTaglibs["/struts-tags"]>

<section class="card_list">

<h1><@wp.i18n key="SEARCH_CARDS" /></h1>

<form action="<@wp.action path="/ExtStr2/do/FrontEnd/Card/search.action" />" method="post" class="form-search" role="search">
	<@wpsf.textfield name="holder" id="holder" cssClass="input-medium search-query" />
	<@s.set name="searchLabel"><@wp.i18n key="SEARCH" /></@s.set>
	<@wpsf.submit value="%{#searchLabel}" cssClass="btn btn-primary" />
</fieldset>

<@wpsa.subset source="cards" count=10 objectName="groupCard" advanced=true offset=5>
<@s.set name="group" value="#groupCard" />

<div class="margin-medium-vertical">
	<@wp.fragment code="portalexample_card_is_pagerInfo" escapeXml=false />
	<@wp.fragment code="portalexample_card_is_pagerFormBlock" escapeXml=false />
</div>

<table class="table table-bordered table-condensed table-hover" summary="<@wp.i18n key="CARDS_LIST_SUMMARY" />">
<thead>
<tr>
	<th><@wp.i18n key="CARD_HOLDER" /></th>
	<th><@wp.i18n key="CARD_DESCRIPTION" /></th>
	<th><@wp.i18n key="CARD_CREATION_DATE" /></th>
</tr>
</thead>
<tbody>
<@s.iterator var="card">
<tr>
	<td><a href="<@wp.action path="/ExtStr2/do/FrontEnd/Card/view.action"><@wp.parameter name="id"><@s.property value="#card.id" /></@wp.parameter></@wp.action>" title="<@wp.i18n key="EDIT" />: <@s.property value="#card.id" />" class="label label-info"><@s.property value="#card.holder" /></a></td>
	<td><@s.property value="#card.descr" /></td>
	<td><@s.date name="#card.date" format="dd/MM/yyyy" /></td>
</tr>
</@s.iterator>
</tbody>
</table>

<div class="centerText">
	<@wp.fragment code="portalexample_card_is_pagerFormBlock" escapeXml=false />
</div>

</@wpsa.subset>

</form>
</section>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('portalexample_card_is_cardView', 'card_list_detail', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign s=JspTaglibs["/struts-tags"]>

<h2><@wp.i18n key="VIEW_CARD" /></h2>

<div class="well">
<dl class="dl-horizontal">
    <dt><@wp.i18n key="CARD_HOLDER" /></dt>
    <dd><@s.property value="holder" /></dd>

    <dt><@wp.i18n key="CARD_DESCRIPTION" /></dt>
    <dd><@s.property value="descr" /></dd>

    <dt><@wp.i18n key="CARD_NOTE" /></dt>
    <dd><@s.property value="note" /></dd>

    <dt><@wp.i18n key="CARD_CREATION_DATE" /></dt>
    <dd><@s.property value="date" /></dd>
</dl>
</div>

<a href="<@wp.action path="/ExtStr2/do/FrontEnd/Card/list.action" />" class="btn"><@wp.i18n key="BACK_TO_CARDS_LIST" /></a>
', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('portalexample_card_is_error', 'card_list_detail', NULL, NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<@wp.i18n key="GENERIC_ERROR" />', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('portalexample_card_is_pagerInfo', 'card_list_detail', NULL, NULL, '<#assign s=JspTaglibs["/struts-tags"]>
<p><@s.text name="note.searchIntro" />&#32;<@s.property value="#group.size" />&#32;<@s.text name="note.searchOutro" />.<br />
<@s.text name="label.page" />: [<@s.property value="#group.currItem" />/<@s.property value="#group.maxItem" />].</p>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('portalexample_card_is_pagerFormBlock', 'card_list_detail', NULL, NULL, '<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<#assign s=JspTaglibs["/struts-tags"]>

<@s.if test="#group.size > #group.max">

<ul class="pagination">
	<@s.if test="null != #group.pagerId">
		<@s.set var="pagerIdMarker" value="#group.pagerId" />
	</@s.if>
	<@s.else>
		<@s.set var="pagerIdMarker">pagerItem</@s.set>
	</@s.else>

	<@s.if test="#group.advanced">
	<li>
		<@wpsf.submit name="%{#pagerIdMarker + ''_1''}" type="button" disabled="%{1 == #group.currItem}" title="%{getText(''label.goToFirst'')}">
			<span class="icon fa fa-step-backward"></span>
		</@wpsf.submit>
	</li>
	<li>
		<@wpsf.submit name="%{#pagerIdMarker + ''_'' + (#group.currItem - #group.offset) }" type="button" disabled="%{1 == #group.beginItemAnchor}" title="%{getText(''label.jump'') + '' '' + #group.offset + '' '' + getText(''label.backward'')}">
			<span class="icon fa fa-fast-backward"></span>
		</@wpsf.submit>
	</li>
	</@s.if>

	<li>
		<@wpsf.submit name="%{#pagerIdMarker + ''_'' + #group.prevItem}" type="button" title="%{getText(''label.prev.full'')}" disabled="%{1 == #group.currItem}">
			<span class="icon fa fa-long-arrow-left"></span>
		</@wpsf.submit>
	</li>

	<@s.subset source="#group.items" count="#group.endItemAnchor-#group.beginItemAnchor+1" start="#group.beginItemAnchor-1">
		<@s.iterator var="item">
			<li>
				<@wpsf.submit name="%{#pagerIdMarker + ''_'' + #item}" type="button" disabled="%{#item == #group.currItem}">
					<@s.property value="%{#item}" />
				</@wpsf.submit>
			</li>
		</@s.iterator>
	</@s.subset>

	<li>
		<@wpsf.submit name="%{#pagerIdMarker + ''_'' + #group.nextItem}" type="button" title="%{getText(''label.next.full'')}" disabled="%{#group.maxItem == #group.currItem}">
			<span class="icon fa fa-long-arrow-right"></span>
		</@wpsf.submit>
	</li>

	<@s.if test="#group.advanced">
	<@s.set var="jumpForwardStep" value="#group.currItem + #group.offset"></@s.set>
	<li>
		<@wpsf.submit name="%{#pagerIdMarker + ''_'' + (#jumpForwardStep)}" type="button" disabled="%{#group.maxItem == #group.endItemAnchor}" title="%{getText(''label.jump'') + '' '' + #group.offset + '' '' + getText(''label.forward'')}">
			<span class="icon fa fa-fast-forward"></span>
		</@wpsf.submit>
	</li>
	<li>
		<@wpsf.submit name="%{#pagerIdMarker + ''_'' + #group.size}" type="button" disabled="%{#group.maxItem == #group.currItem}" title="%{getText(''label.goToLast'')}">
			<span class="icon fa fa-step-forward"></span>
		</@wpsf.submit>
	</li>
	</@s.if>

	<@s.set var="pagerIdMarker" value="null" />
</ul>

</@s.if>', 1);

