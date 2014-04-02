/*
 * File: app/view/MainView.js
 *
 * This file was generated by Sencha Architect version 3.0.1.
 * http://www.sencha.com/products/architect/
 *
 * This file requires use of the Sencha Touch 2.3.x library, under independent license.
 * License of Sencha Architect does not include license for Sencha Touch 2.3.x. For more
 * details see http://www.sencha.com/license or contact license@sencha.com.
 *
 * This file will be auto-generated each and everytime you save your project.
 *
 * Do NOT hand edit this file.
 */

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
                title: '${entity.name}s',
                iconCls: 'info',
                itemId: '${entity.lowerCase}s',
                layout: 'fit',
                items: [
                    {
                        xtype: '${entity.lowerCase}NavigationView'
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
