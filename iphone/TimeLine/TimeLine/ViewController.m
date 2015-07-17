//
//  ViewController.m
//  TimeLine
//
//  Created by Developer on 15/6/24.
//  Copyright (c) 2015年 Developer. All rights reserved.
//

#import "ViewController.h"
#import "DayThing.h"

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    DayThing *thing;
    thing = [[DayThing alloc] initWithFrame:CGRectMake(0,0,100,50)];
    [self.view addSubview:thing];
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
