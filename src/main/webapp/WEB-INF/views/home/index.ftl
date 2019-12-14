<#ftl output_format="HTML">

<@content for="title">Javalite demo app</@content>

<h2>Javalite demo application</h2>

<ul>
    <li><@link_to controller="contests">Contest CRUD</@link_to></li>
    <li><@link_to controller="stats" query_string="s=avg">Running Statistics</@link_to></li>
</ul>

