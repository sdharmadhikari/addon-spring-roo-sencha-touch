/*
 * File: app/model/${entity.name}.js
 *
 * This file was generated by Sencha Architect version 3.0.4.
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

Ext.define('${app.name}.model.${entity.name}', {
    extend: 'Ext.data.Model',

    requires: [
        'Ext.data.proxy.Rest',
        'Ext.data.Field'
    ],

    config: {
        proxy: {
            type: 'rest',
            url: 'http://localhost:8080/${app.name}/${entity.lowerCase}s',
            headers: {
                Accept: 'application/json'
            }
        },
        fields: [
            {
                name: 'attrName'
            },
            {
                name: 'version'
            }
        ]
    }
});
