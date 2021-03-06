/*
 * File: app/store/${entity.name}JsonPStore.js
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

Ext.define('${app.name}.store.${entity.name}JsonPStore', {
    extend: 'Ext.data.Store',

    requires: [
        '${app.name}.model.${entity.name}',
        'Ext.data.proxy.Rest',
        'Ext.data.reader.Json'
    ],

    config: {
        autoLoad: true,
        autoSync: true,
        model: '${app.name}.model.${entity.name}',
        storeId: '${entity.name}JsonPStore',
        proxy: {
            type: 'rest',
            url: 'http://localhost:8080/${app.name}/${entity.lowerCase}s',
            reader: {
                type: 'json'
            }
        }
    }
});
