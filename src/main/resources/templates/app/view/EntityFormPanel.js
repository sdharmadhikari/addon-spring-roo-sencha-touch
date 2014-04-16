/*
 * File: app/view/${entity.name}FormPanel.js
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

Ext.define('${app.name}.view.${entity.name}FormPanel', {
    extend: 'Ext.form.Panel',
    alias: 'widget.${entity.lowerCase}FormPanel',

    requires: [
        'Ext.form.FieldSet',
        'Ext.field.Text',
        'Ext.Button'
    ],

    config: {
        itemId: '${entity.lowerCase}FormPanel',
        items: [
            {
                xtype: 'fieldset',
                itemId: 'attrNameFieldSetItemId',
                items: [
                    {
                        xtype: 'textfield',
                        label: 'attrName',
                        labelWidth: '40%',
                        name: 'attrName'
                    }
                ]
            },
            {
                xtype: 'fieldset',
                itemId: '${entity.lowerCase}SaveFieldSetItemId',
                items: [
                    {
                        xtype: 'button',
                        itemId: '${entity.lowerCase}SaveButton',
                        ui: 'action',
                        iconCls: '',
                        text: 'Save'
                    }
                ]
            },
            {
                xtype: 'fieldset',
                itemId: '${entity.lowerCase}DeleteFieldSetItemId',
                items: [
                    {
                        xtype: 'button',
                        itemId: '${entity.lowerCase}DeleteButton',
                        ui: 'decline',
                        iconAlign: 'center',
                        iconCls: 'delete'
                    }
                ]
            }
        ]
    }

});
