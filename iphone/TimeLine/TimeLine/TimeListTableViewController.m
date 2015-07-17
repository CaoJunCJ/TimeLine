//
//  TimeListTableViewController.m
//  TimeLine
//
//  Created by Developer on 15/6/24.
//  Copyright (c) 2015年 Developer. All rights reserved.
//

#import "TimeListTableViewController.h"
#import "DayThing.h"

@interface TimeListTableViewController ()
{
    NSArray *arrayData;
}
@end

@implementation TimeListTableViewController



- (void)viewDidLoad {
    [super viewDidLoad];
    NSString *path = [[NSBundle mainBundle] pathForResource:@"data" ofType:@"json"];
    NSString *jsonString = [[NSString alloc] initWithContentsOfFile:path encoding:NSUTF8StringEncoding error:nil];
    NSLog(@"data: %@", jsonString);
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData options:NSJSONReadingMutableLeaves error:nil];
    arrayData = [dic objectForKey:@"data"];
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (arrayData) {
        return arrayData.count;
    } else {
       return 0;
    }
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cell" forIndexPath:indexPath];
    NSDictionary *dateDic = [arrayData objectAtIndex:indexPath.row];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cell"];
    }
    
    if (dateDic) {
        DayThing *dg = (DayThing *)[cell viewWithTag:2000];
        if (dg) {
            if (![dg isInitJsonData]) {
                [dg initJsonData:dateDic];
            }
            
        }else{
            dg = [[DayThing alloc] initWithFrame:CGRectMake(0 , 0, cell.bounds.size.width, [self tableView:tableView heightForRowAtIndexPath:indexPath]) AndJson:dateDic];
            dg.tag = 2000;
            [cell addSubview:dg];
        }
    }
    
    /*
    if (arrayData) {
        NSDictionary *dateDic = [arrayData objectAtIndex:indexPath.row];
        DayThing *thing = [[DayThing alloc] initWithFrame:CGRectMake(0 , 0, cell.bounds.size.width, [self tableView:tableView heightForRowAtIndexPath:indexPath]) AndJson:dateDic];
        thing.tag = 2000;
        [cell addSubview:thing];
    }
    */
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    //calc table cell height
    int height = 0;
    if (arrayData) {
        NSDictionary *dateDic = [arrayData objectAtIndex:indexPath.row];
        NSArray *arrayThings = [dateDic objectForKey:@"things"];
        height += 30;
        height += 30;
        for (int i = 0; i < arrayThings.count; i++) {
            height += 50;
            height += 12;
        }
        height += 30;
    }
    
    
    return height;
}


/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    } else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath {
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath {
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
