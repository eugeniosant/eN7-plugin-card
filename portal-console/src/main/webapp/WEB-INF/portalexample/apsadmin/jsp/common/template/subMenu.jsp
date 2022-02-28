<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<wpsa:ifauthorized permission="superuser">
    <li class="list-group-item" role="presentation">
        <a role="menuitem" href="<s:url action="list" namespace="/do/Card" />" >
            <span class="list-group-item-value">CARD</span>
        </a>
    </li>
</wpsa:ifauthorized>
