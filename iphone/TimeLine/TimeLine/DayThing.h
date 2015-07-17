//
//  DayThing.h
//  TimeLine
//
//  Created by Developer on 15/6/24.
//  Copyright (c) 2015年 Developer. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DayThing : UIView
- (id)initWithFrame:(CGRect)frame AndJson:(NSDictionary *)jsonDic;
- (void)initJsonData:(NSDictionary *)jsonDic;
- (bool) isInitJsonData;
@end
