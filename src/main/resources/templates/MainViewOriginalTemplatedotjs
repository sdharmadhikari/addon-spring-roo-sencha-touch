

Ext.define('${app.name}.view.MainView', {
    extend: 'Ext.tab.Panel',

    requires: [
        '${app.name}.view.${entity.name}NavigationView',
        'Ext.navigation.View',
        'Ext.tab.Bar'
    ],

    config: {
        itemId: 'mainView',
        items: [
            {
                xtype: 'container',
                title: '$entity.plural',
                iconCls: 'info',
                itemId: '$entity.plural',
                layout: 'fit',
                items: [
                    {
                        xtype: '${entity.name}NavigationView'
                    }
                ]
            }
        ],
        tabBar: {
            docked: 'bottom',
            scrollable: true
        }
    }

});