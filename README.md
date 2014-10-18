#BakaTxt user/dev guide

##Generating the HTML

The user/dev guide is written in markdown.  
The HTML is generated via [MacDown](http://macdown.uranusjr.com) from the markdown source.  
Please use the given `GitHub2.1.css` theme for MacDown, also, you will need to place the `prism-custom.css` within the MacDown package (`Contents/Resources/Prism/themes`) and use that as your syntax highlighter.  
A header (in `header.html`) needs to be manually added to the generated HTML (for custom fonts and some other stuff). 

## Misc.

You may add the following code to place page breaks (for print):  
```html
<div class="page-break"></div>
```
The best way to print (to pdf or paper) that I have found would be via OSX Firefox, which lets you choose the location and contents of the header and footer. Note that the header and footer will only use Myriad Pro as a font, I am unable to find a setting to change that.
