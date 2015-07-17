//
//  Header.h
//  TimeLine
//
//  Created by Developer on 15/6/25.
//  Copyright (c) 2015年 Developer. All rights reserved.
//

#ifndef TimeLine_Header_h
#define TimeLine_Header_h

#define UIColorFromRGB(rgbValue) \
[UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 \
green:((float)((rgbValue & 0x00FF00) >>  8))/255.0 \
blue:((float)((rgbValue & 0x0000FF) >>  0))/255.0 \
alpha:1.0]

#endif
