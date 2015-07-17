//
//  DayThing.m
//  TimeLine
//
//  Created by Developer on 15/6/24.
//  Copyright (c) 2015年 Developer. All rights reserved.
//

#import "DayThing.h"
#import "ColorUtil.h"

@interface DayThing ()
{
    NSArray *arrayThings;
    NSString *date;
    CGContextRef context;
    NSString *year;
    NSString *month;
    NSString *dayOfMonth;
    NSString *week;
    double currentDrawHeight;
    bool isInitJsonData;
}
@end

int SW_WeekDay(int   year,   int   month,   int   day)
{
    int   DayOfWeek;
    DayOfWeek   =   year   >   0   ?   (5   +   (year   +   1)   +   (year   -   1)/4   -   (year   -   1)/100   +   (year   -   1)/400)   %   7
    :   (5   +   year   +   year/4   -   year/100   +   year/400)   %   7;
    DayOfWeek   =   month   >   2   ?   (DayOfWeek   +   2*(month   +   1)   +   3*(month   +   1)/5)   %   7
    :   (DayOfWeek   +   2*(month   +   2)   +   3*(month   +   2)/5)   %   7;
    if   (((year%4   ==   0   &&   year%100   !=   0)   ||   year%400   ==   0)   &&   month> 2)
    {
        DayOfWeek   =   (DayOfWeek   +   1)   %   7;
    }
    DayOfWeek   =   (DayOfWeek   +   day)   %   7;
    return   DayOfWeek;
}

@implementation DayThing

- (void)initJsonData:(NSDictionary *)jsonDic
{
    currentDrawHeight = 0;
    date = [jsonDic objectForKey:@"date"];
    arrayThings = [jsonDic objectForKey:@"things"];
    if (date) {
        NSArray *array = [date componentsSeparatedByString:@"."];
        if ( array != nil && array.count > 2) {
            year = [array objectAtIndex:0];
            month = [array objectAtIndex:1];
            dayOfMonth = [array objectAtIndex:2];
            
            int weekIndex = SW_WeekDay([year intValue], [month intValue], [dayOfMonth intValue]);
            switch (weekIndex) {
                case 0:
                    week = @"星期天";
                    break;
                case 1:
                    week = @"星期一";
                    break;
                case 2:
                    week = @"星期二";
                    break;
                case 3:
                    week = @"星期三";
                    break;
                case 4:
                    week = @"星期四";
                    break;
                case 5:
                    week = @"星期五";
                    break;
                case 6:
                    week = @"星期六";
                    break;
                    
                default:
                    break;
            }
            
            /*
             NSDateFormatter *format = [[NSDateFormatter alloc] init];
             NSTimeZone *timeZone = [NSTimeZone localTimeZone];
             [format setTimeZone:timeZone];
             [format setDateFormat:@"M/d/yyyy H:m a"];
             NSString *stringTime = [NSString stringWithFormat:@"%@/%@/%@ 12:00 am", month, dayOfMonth, year];
             NSDate *dateTime = [format dateFromString:stringTime];
             */
            
            
            isInitJsonData = true;
            NSLog(@"%@年%@月%@日", year, month, dayOfMonth);
        }
        
    }

}

- (bool) isInitJsonData
{
    return isInitJsonData;
}

- (id)initWithFrame:(CGRect)frame AndJson:(NSDictionary *)jsonDic{
    //calc view height by jsonArray at TableViewController.
    self = [super initWithFrame:frame];
    if (self) {
        [self initJsonData:jsonDic];
    }
    return self;
}

// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    
    self.backgroundColor = [UIColor whiteColor];
    [self.backgroundColor setFill];
    CGContextFillRect(UIGraphicsGetCurrentContext(), rect);
    currentDrawHeight = 0;
    
    //set backgroundColor to white.
    
    
    //set antialias and SmoothFonts to context.
    context = UIGraphicsGetCurrentContext();
    CGContextSetShouldAntialias(context, true);
    CGContextSetShouldSmoothFonts(context, true);
    /*
    [self drawLineMarginLeft:50 AndMarginTop:currentDrawHeight AndLength:30];
    [self drawStrokeCircleMarginLeft:50 AndMarginTop:currentDrawHeight AndRadius:30];
    [self drawLineMarginLeft:50 AndMarginTop:currentDrawHeight AndLength:30];
    [self drawFillCircleMarginLeft:50 AndMarginTop:currentDrawHeight AndRadius:12 AndTitle:@"title" AndTime:@"12:15"];
    [self drawLineMarginLeft:50 AndMarginTop:currentDrawHeight AndLength:30];
    */
    
    [self drawLineMarginLeft:55 AndMarginTop:currentDrawHeight AndLength:30];
    [self drawStrokeCircleMarginLeft:55 AndMarginTop:currentDrawHeight AndRadius:30];
    for (int i = 0; i < arrayThings.count; i++) {
        NSDictionary *timeThings = [arrayThings objectAtIndex:i];
        NSString *time = [timeThings objectForKey:@"time"];
        NSString *title = [timeThings objectForKey:@"title"];
        NSArray *timeSqlit = [time componentsSeparatedByString:@":"];
        NSString *hourAndMinute = [NSString stringWithFormat:@"%@:%@", timeSqlit[0], timeSqlit[1]];
        [self drawLineMarginLeft:55 AndMarginTop:currentDrawHeight AndLength:50];
        [self drawFillCircleMarginLeft:55 AndMarginTop:currentDrawHeight AndRadius:12 AndTitle:title AndTime:hourAndMinute];
    }
    [self drawLineMarginLeft:55 AndMarginTop:currentDrawHeight AndLength:30];
    
    /*
    CGRect bounds = [self bounds];
    CGPoint center;
    center.x = bounds.origin.x + bounds.size.width / 2.0;
    center.y = bounds.origin.y + bounds.size.height / 2.0;
    UIFont *font = [UIFont boldSystemFontOfSize:28];
    CGRect textRect;
    textRect.size = [date sizeWithAttributes:@{NSFontAttributeName:font}];
    textRect.origin.x = center.x - textRect.size.width / 2.0;
    textRect.origin.y = center.y - textRect.size.height / 2.0;
    
    [[UIColor blackColor] setFill];
    CGSize offset = CGSizeMake(4, 3);
    CGColorRef color = [[UIColor darkGrayColor] CGColor];
    CGContextSetShadowWithColor(context, offset, 2.0, color);
    
    [date drawInRect:textRect withAttributes:@{NSFontAttributeName:font}];
    */
}

- (void) drawLineMarginLeft:(double)mLeft AndMarginTop:(double)mTop AndLength:(double)length
{
    CGContextSetFillColorWithColor(context, [UIColorFromRGB(0xD3D3D3) CGColor]);
    CGContextSetStrokeColorWithColor(context, [UIColorFromRGB(0xD3D3D3) CGColor]);
    CGContextSetLineWidth(context, 3);
    CGPoint points[2];
    points[0] = CGPointMake(mLeft, mTop);
    points[1] = CGPointMake(mLeft, mTop + length);
    CGContextAddLines(context, points, 2);
    CGContextDrawPath(context, kCGPathStroke);
    currentDrawHeight += length;
}

- (void) drawStrokeCircleMarginLeft:(double)mLeft AndMarginTop:(double)mTop AndRadius:(double)length
{
    CGContextSetFillColorWithColor(context, [UIColorFromRGB(0xD3D3D3) CGColor]);
    CGContextSetStrokeColorWithColor(context, [UIColorFromRGB(0xD3D3D3) CGColor]);
    CGContextSetLineWidth(context, 2.5);
    CGRect rect = CGRectMake(mLeft - (length/2), mTop, length, length);
    CGContextAddEllipseInRect(context, rect);
    CGContextStrokePath(context);
    
    CGRect textRect;
    UIFont *font = [UIFont boldSystemFontOfSize:14];
    textRect.size = [dayOfMonth sizeWithAttributes:@{NSFontAttributeName:font}];
    textRect.origin.x = (rect.origin.x + rect.size.width/2) - textRect.size.width / 2.0;
    textRect.origin.y = (rect.origin.y + rect.size.height/2) - textRect.size.height / 2.0;
    [dayOfMonth drawInRect:textRect withAttributes:@{NSFontAttributeName:font,
                                                     NSForegroundColorAttributeName: [UIColor blackColor]}];
    
    NSString *dateString = [NSString stringWithFormat:@"%@年%@月 %@", year, month, week];
    textRect.size = [dateString sizeWithAttributes:@{NSFontAttributeName:font}];
    textRect.origin.x = (rect.origin.x + rect.size.width) + 20;
    [dateString drawInRect:textRect withAttributes:@{NSFontAttributeName:font,
                                                     NSForegroundColorAttributeName: [UIColor grayColor]}];
    currentDrawHeight += length;
    //CGContextFillPath(context);
    //CGContextDrawPath(context, kCGPathStroke);
}

- (void) drawFillCircleMarginLeft:(double)mLeft AndMarginTop:(double)mTop AndRadius:(double)length AndTitle:(NSString *)title AndTime:(NSString *)time
{
    CGContextSetFillColorWithColor(context, [UIColorFromRGB(0xADD8E6) CGColor]);
    CGContextSetStrokeColorWithColor(context, [UIColorFromRGB(0xADD8E6) CGColor]);
    CGContextSetLineWidth(context, 1);
    CGRect rect = CGRectMake(mLeft - (length/2) + 1.5 , mTop + 1.5 , length -3 , length - 3);
    CGContextAddEllipseInRect(context, rect);
    CGContextFillPath(context);
    
    CGContextSetStrokeColorWithColor(context, [UIColorFromRGB(0x0099CC) CGColor]);
    CGContextSetLineWidth(context, 2);
    UIBezierPath *path;
    CGRect titleBoxRect = CGRectMake(rect.origin.x + length/2 + 20, rect.origin.y - 20 + length/2, 200, 40);
    path = [UIBezierPath bezierPathWithRoundedRect:titleBoxRect byRoundingCorners:UIRectCornerAllCorners cornerRadii:CGSizeMake(8, 8)];
    [path stroke];
    
    CGContextSetStrokeColorWithColor(context, [UIColorFromRGB(0xFFFFFF) CGColor]);
    
    CGPoint points[] = {CGPointMake(rect.origin.x + length/2 + 20, rect.origin.y + length/2 - 5), CGPointMake(rect.origin.x + length/2 + 20, rect.origin.y + length/2 - 5 + 10)};
    CGContextAddLines(context, points, 2);
    CGContextDrawPath(context, kCGPathStroke);
    
    CGContextSetLineWidth(context, 1);
    CGContextSetStrokeColorWithColor(context, [UIColorFromRGB(0x0099CC) CGColor]);
    CGPoint points1[] = {CGPointMake(rect.origin.x + length/2 + 20, rect.origin.y + length/2 - 5), CGPointMake(rect.origin.x + length/2 + 20 - 10, rect.origin.y + length/2 - 5 + 5)};
    CGContextAddLines(context, points1, 2);
    CGContextDrawPath(context, kCGPathStroke);
    
    CGContextSetLineWidth(context, 1);
    CGPoint points2[] = {CGPointMake(rect.origin.x + length/2 + 20 - 10, rect.origin.y + length/2 - 5 + 5), CGPointMake(rect.origin.x + length/2 + 20, rect.origin.y + length/2 - 5 + 10)};
    CGContextAddLines(context, points2, 2);
    CGContextDrawPath(context, kCGPathStroke);
    
    CGRect textRect;
    UIFont *font = [UIFont boldSystemFontOfSize:16];
    textRect.size = [title sizeWithAttributes:@{NSFontAttributeName:font}];
    textRect.origin.x = (titleBoxRect.origin.x + titleBoxRect.size.width/2) - textRect.size.width / 2.0;
    textRect.origin.y = (titleBoxRect.origin.y + titleBoxRect.size.height/2) - textRect.size.height / 2.0;
    [title drawInRect:textRect withAttributes:@{NSFontAttributeName:font,
                                                     NSForegroundColorAttributeName: UIColorFromRGB(0x33B5E5)}];
    
    CGRect timeRect;
    UIFont *timeFont = [UIFont boldSystemFontOfSize:10];
    timeRect.size = [time sizeWithAttributes:@{NSFontAttributeName:timeFont}];
    timeRect.origin.x = rect.origin.x - timeRect.size.width - 8;
    timeRect.origin.y = (rect.origin.y + rect.size.height/2) - timeRect.size.height / 2.0;
    [time drawInRect:timeRect withAttributes:@{NSFontAttributeName:timeFont,
                                                NSForegroundColorAttributeName: [UIColor grayColor]}];
    
    //CGContextClearRect(context, CGRectMake(rect.origin.x + length/2 + 20, rect.origin.y + length/2 - 4, 2, 8));
    currentDrawHeight += length;
}


@end
