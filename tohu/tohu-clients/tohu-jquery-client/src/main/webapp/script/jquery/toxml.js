/**
 * XML Serializer plugin
 *
 * By Mark Gibson
 *
 * Returns a string containing the serialized XML representation of the DOM elements.
 *
 * Usage: var xmlString = $('#something').toXML();
 */

jQuery.fn.toXML = function ()
{
  var toXML = function(node)
  {
    var out = '';
    var attributes = '';
    var content = '';
    out += '<' + node.nodeName;
    if (node.childNodes)
    {
      for (var i = 0; i < node.childNodes.length; i++)
      {
        switch(node.childNodes[i].nodeType)
        {
          case 1:     // ELEMENT_NODE
            content += toXML(node.childNodes[i]);
            break;
          case 2:     // ATTRIBUTE_NODE
            attributes += ' ' + node.childNodes[i].nodeName  + '="'
                              + node.childNodes[i].nodeValue + '"';
            break;
          case 3:     // TEXT_NODE
          case 4:     // CDATA_SECTION_NODE
          case 5:     // ENTITY_REFERENCE_NODE
          case 6:     // ENTITY_NODE
          case 7:     // PROCESSING_INSTRUCTION_NODE
          case 8:     // COMMENT_NODE
          case 9:     // DOCUMENT_NODE
          case 10:    // DOCUMENT_TYPE_NODE
          case 11:    // DOCUMENT_FRAGMENT_NODE
          case 12:    // NOTATION_NODE
            content += node.childNodes[i].nodeValue;
            break;
        }
      }
    }
    out += attributes;
    if (content.length > 0)
    {
      out += '>' + content;
      out += '</' + node.tagName + '>';
    }
    else
    {
      out += '/>';
    }
    return out;
  }
  var out = '';
  if (this.length > 0) {
    if (typeof XMLSerializer == 'function' ||
        typeof XMLSerializer == 'object')
    {
      var xs = new XMLSerializer();
      this.each(function() { out += xs.serializeToString(this); });
    }
    else if (this[0].xml !== undefined)
    {
      this.each(function() { out += this.xml; });
    }
    else
    {
      if (this.length > 0)
      {
        this.each( function() { out += toXML(this); } );
      }
    }
  }
  return out;
};
